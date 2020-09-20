package com.test.bidservice.service

import com.test.bidservice.model.request.BidRequest
import com.test.bidservice.model.response.BannerWithPrice

trait BidRequestService {
  def processBid(bidRequest: BidRequest): Option[(String, List[BannerWithPrice])]
}
