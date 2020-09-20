package com.test.bidservice.model.response

case class BidResponse(id: String,
                       bidRequestId: String,
                       adId: Option[String],
                       banner: List[BannerWithPrice])
