package com.test.bidservice.service

import akka.http.scaladsl.unmarshalling.FromEntityUnmarshaller
import com.test.bidservice.config.DspConfig
import com.test.bidservice.model.response.{BidderResponse, ResponseAggregationResult}

import scala.concurrent.Future

trait BidRequestSenderService {
  def sendBidRequest(dsps: Seq[DspConfig]): Future[ResponseAggregationResult]
}
