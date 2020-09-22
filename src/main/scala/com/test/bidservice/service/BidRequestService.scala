package com.test.bidservice.service

import com.test.bidservice.model.campaign.Banner
import com.test.bidservice.model.request.BidRequest

trait BidRequestService {
  def processBid(bidRequest: BidRequest): Option[(String, Double, Banner)]
}
