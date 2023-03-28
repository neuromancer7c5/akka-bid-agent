package com.test.bidservice.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import com.test.bidservice.model.response.AuctionResponse
import com.test.bidservice.service.AuctionService

object AuctionActor {
  final case class StartAuction(replyTo: ActorRef[GetAuctionResponse])

  final case class GetAuctionResponse(auctionResponse: AuctionResponse)

  def apply(auctionService: AuctionService): Behavior[StartAuction] = bidAgent(auctionService)

  private def bidAgent(auctionService: AuctionService): Behavior[StartAuction] =
    Behaviors.receive[StartAuction] { case (context, message) =>
      implicit val ex = context.system.executionContext
      auctionService.startAuction().map { response =>
        message.replyTo ! GetAuctionResponse(response)
      }
      Behaviors.same
    }
}
