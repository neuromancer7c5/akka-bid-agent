package com.test.bidservice.route

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import akka.util.Timeout
import com.test.bidservice.actor.BidActor
import com.test.bidservice.actor.BidActor.{GetBidResponse, ProcessBid}
import com.test.bidservice.model.request.BidRequest
import com.test.bidservice.util.JsonSupport

import scala.concurrent.Future


class BidRoutes(bidRegistry: ActorRef[BidActor.ProcessBid])
               (implicit val system: ActorSystem[_])
  extends Directives
    with JsonSupport  {

  private implicit val timeout: Timeout =
    Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def processBid(bidRequest: BidRequest): Future[GetBidResponse] =
    bidRegistry.ask(ProcessBid(bidRequest, _))

  val bidRoutes: Route =
    pathPrefix("bids") {
      pathEnd {
        post {
          entity(as[BidRequest]) { bidRequest =>
            onSuccess(processBid(bidRequest)) { resp =>
              resp.optionBidResonse match {
                case None => complete(StatusCodes.NoContent)
                case Some(response) => complete(response)
              }
            }
          }
        }
      }
    }
}
