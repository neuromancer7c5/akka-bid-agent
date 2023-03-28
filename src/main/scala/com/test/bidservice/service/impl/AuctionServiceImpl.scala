package com.test.bidservice.service.impl

import akka.actor.typed.{ActorSystem, DispatcherSelector}
import akka.stream.Materializer
import com.test.bidservice.config.DspConfig
import com.test.bidservice.model.response.AuctionResponse
import com.test.bidservice.service.{AuctionService, BidRequestSenderService}
import com.test.bidservice.util.Logger

import scala.concurrent.Future

class AuctionServiceImpl(requestSenderService: BidRequestSenderService, dsps: Seq[DspConfig])(implicit val system: ActorSystem[_])
  extends AuctionService with Logger {
  implicit val classicSystem = system.classicSystem
  implicit val ex = system.dispatchers.lookup(DispatcherSelector.blocking())
  implicit val mat = Materializer.createMaterializer(classicSystem)

  override def startAuction(): Future[AuctionResponse] = {
    requestSenderService.sendBidRequest(dsps)
      .map(_.result.maxBy(_._2.price))
      .map(result => AuctionResponse(result._2.price, result._1))
  }
}
