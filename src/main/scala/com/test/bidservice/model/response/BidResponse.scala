package com.test.bidservice.model.response

import com.test.bidservice.model.campaign.Banner

case class BidResponse(id: String,
                       bidRequestId: String,
                       price: Double,
                       adId: Option[String],
                       banner: List[Banner])
