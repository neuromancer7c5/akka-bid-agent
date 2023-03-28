package com.test.bidservice.service

import com.test.bidservice.model.response.BidderResponse

trait BidderService {
  def handleBid(price: Double): BidderResponse
}
