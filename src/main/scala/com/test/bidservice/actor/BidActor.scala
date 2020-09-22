package com.test.bidservice.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import com.test.bidservice.model.request.BidRequest
import com.test.bidservice.model.response.BidResponse
import com.test.bidservice.service.BidRequestService

object BidActor {
  final case class ProcessBid(bidRequest: BidRequest, replyTo: ActorRef[GetBidResponse])

  final case class GetBidResponse(bidResonseOpt: Option[BidResponse])

  def apply(bidRequestValidator: BidRequestService): Behavior[ProcessBid] = bidAgent(0, bidRequestValidator)

  val responseName = "response"

  private def bidAgent(bidCounter: Int, bidRequestValidator: BidRequestService): Behavior[ProcessBid] =
    Behaviors.receiveMessage { message =>
      val n = bidCounter + 1
      val response = bidRequestValidator.processBid(message.bidRequest).map { result =>
        BidResponse(
          id = responseName + n.toString,
          bidRequestId = message.bidRequest.id,
          price = result._2,
          adId = Some(result._1),
          banner = Some(result._3)
        )
      }
      message.replyTo ! GetBidResponse(response)
      bidAgent(n, bidRequestValidator)
    }
}
