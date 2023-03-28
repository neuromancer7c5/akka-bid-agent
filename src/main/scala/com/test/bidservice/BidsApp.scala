package com.test.bidservice

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import com.test.bidservice.actor.{AuctionActor, BidActor, BidderActor}
import com.test.bidservice.config.DspConfig
import com.test.bidservice.route.BidRoutes
import com.test.bidservice.service.impl.{AuctionServiceImpl, BidRequestSenderServiceImpl, BidRequestServiceImpl, BidderServiceImpl}
import com.test.bidservice.util.CampaignHelper

import scala.util.Failure
import scala.util.Success

object BidsApp {
  private def startHttpServer(routes: Route, system: ActorSystem[_]): Unit = {
    implicit val classicSystem: akka.actor.ActorSystem = system.toClassic
    import system.executionContext
    val futureBinding = Http().bindAndHandle(
      routes,
      system.settings.config.getString("my-app.host"),
      system.settings.config.getInt("my-app.port")
    )
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }
  def main(args: Array[String]): Unit = {
    val rootBehavior = Behaviors.setup[Nothing] { context =>
      implicit val system: ActorSystem[Nothing] = context.system
      val price = system.settings.config.getDouble("my-app.dsps.default-price")
      val ids = system.settings.config.getString("my-app.dsps.ids")
      val dsps = ids.split(",").toSeq.map{id =>
        val dspConfig = system.settings.config.getConfig(s"my-app.dsps.$id")
        DspConfig.fromConfig(dspConfig)
      }
      val bidRequestValidator = new BidRequestServiceImpl(CampaignHelper.getCampaigns)
      val bidRegistryActor = context.spawn(BidActor(bidRequestValidator), "BidRegistryActor")
      context.watch(bidRegistryActor)

      val bidderService = new BidderServiceImpl
      val bidderActor = context.spawn(BidderActor(bidderService), "BidderRegistryActor")
      context.watch(bidRegistryActor)

      val requestSenderService = new BidRequestSenderServiceImpl(price)
      val auctionService = new AuctionServiceImpl(requestSenderService, dsps)
      val auctionActor = context.spawn(AuctionActor(auctionService), "AuctionRegistryActor")
      context.watch(bidRegistryActor)

      val routes = new BidRoutes(bidRegistryActor, bidderActor, auctionActor)(context.system)
      startHttpServer(routes.bidRoutes, context.system)

      Behaviors.empty
    }
   ActorSystem[Nothing](rootBehavior, "BidsAkkaHttpServer")
  }
}