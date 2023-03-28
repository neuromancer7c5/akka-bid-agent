package com.test.bidservice.service

import com.test.bidservice.model.response.AuctionResponse

import scala.concurrent.Future

trait AuctionService {
  def startAuction(): Future[AuctionResponse]
}
