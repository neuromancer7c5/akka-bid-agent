package com.test.bidservice.model.response

import com.test.bidservice.model.campaign.Banner

case class BannerWithPrice(id: Int,
                           src: String,
                           width: Int,
                           height: Int,
                           price: Double)

object BannerWithPriceBuilder{
  def apply(banner: Banner, price: Double):
  BannerWithPrice = BannerWithPrice(
    banner.id,
    banner.src,
    banner.width,
    banner.height,
    price)
}
