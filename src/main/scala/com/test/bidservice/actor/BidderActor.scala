package com.test.bidservice.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import com.test.bidservice.model.response.BidderResponse
import com.test.bidservice.service.BidderService

object BidderActor {
  final case class HandleBid(price: Double, replyTo: ActorRef[GetBidderResponse])

  final case class GetBidderResponse(bidderResponse: BidderResponse)

  def apply(bidderService: BidderService): Behavior[HandleBid] = bidAgent(bidderService)

  private def bidAgent(bidderService: BidderService): Behavior[HandleBid] =
    Behaviors.receiveMessage[HandleBid] { message =>
      message.replyTo ! GetBidderResponse(bidderService.handleBid(message.price))
      Behaviors.same
    }
}
