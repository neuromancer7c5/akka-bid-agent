package com.test.bidservice.route

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import akka.util.Timeout
import com.test.bidservice.actor.AuctionActor.{GetAuctionResponse, StartAuction}
import com.test.bidservice.actor.BidActor.{GetBidResponse, ProcessBid}
import com.test.bidservice.actor.BidderActor.{GetBidderResponse, HandleBid}
import com.test.bidservice.actor.{AuctionActor, BidActor, BidderActor}
import com.test.bidservice.model.request.BidRequest
import com.test.bidservice.util.JsonSupport

import scala.concurrent.Future


class BidRoutes(bidRegistry: ActorRef[BidActor.ProcessBid],
                bidderRegistry: ActorRef[BidderActor.HandleBid],
                auctionRegistry: ActorRef[AuctionActor.StartAuction])
               (implicit val system: ActorSystem[_])
  extends Directives
    with JsonSupport {

  private implicit val timeout: Timeout =
    Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def processBid(bidRequest: BidRequest): Future[GetBidResponse] =
    bidRegistry.ask(ProcessBid(bidRequest, _))

  def processBidder(price: Double): Future[GetBidderResponse] =
    bidderRegistry.ask(HandleBid(price, _))

  def processAuction(): Future[GetAuctionResponse] =
    auctionRegistry.ask(StartAuction)

  val bidRoutes: Route =
    pathPrefix(RoutePathNames.BidRequest) {
      pathEnd {
        post {
          entity(as[BidRequest]) { bidRequest =>
            onSuccess(processBid(bidRequest)) { resp =>
              resp.bidResonseOpt match {
                case None => complete(StatusCodes.NoContent)
                case Some(response) => complete(response)
              }
            }
          }
        }
      }
    } ~
  pathPrefix(RoutePathNames.Bidder) {
    pathEnd {
      post {
        parameters(QueryParameters.Price.as[Double], QueryParameters.Dsp) { (price, _) =>
          onSuccess(processBidder(price)) { resp =>
            complete(resp.bidderResponse)
          }
        }
      }
    }
  } ~
  pathPrefix(RoutePathNames.Auction) {
    pathEnd {
      post {
        onSuccess(processAuction()) { resp =>
          complete(resp.auctionResponse)
        }
      }
    }
  }
}
