package com.test.bidservice

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import com.test.bidservice.actor.BidActor
import com.test.bidservice.route.BidRoutes
import com.test.bidservice.service.impl.BidRequestServiceImpl
import com.test.bidservice.util.CampaignHelper

import scala.util.Failure
import scala.util.Success

object BidsApp {
  private def startHttpServer(routes: Route, system: ActorSystem[_]): Unit = {
    implicit val classicSystem: akka.actor.ActorSystem = system.toClassic
    import system.executionContext

    val futureBinding = Http().bindAndHandle(routes, "localhost", 8080)
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
      val bidRequestValidator = new BidRequestServiceImpl(CampaignHelper.getCampaigns)
      val bidRegistryActor = context.spawn(BidActor(bidRequestValidator), "BidRegistryActor")
      context.watch(bidRegistryActor)

      val routes = new BidRoutes(bidRegistryActor)(context.system)
      startHttpServer(routes.bidRoutes, context.system)

      Behaviors.empty
    }
   ActorSystem[Nothing](rootBehavior, "BidsAkkaHttpServer")
  }
}