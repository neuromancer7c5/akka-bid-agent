package com.test.bidservice.util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.test.bidservice.model.campaign.Banner
import com.test.bidservice.model.request._
import com.test.bidservice.model.response.{BannerWithPrice, BidResponse}
import spray.json.{DefaultJsonProtocol, PrettyPrinter}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val printer = PrettyPrinter
  implicit val geoJsonFormat = jsonFormat1(Geo)
  implicit val bidUserJsonFormat = jsonFormat2(BidUser)
  implicit val siteJsonFormat = jsonFormat2(Site)
  implicit val deviceJsonFormat = jsonFormat2(Device)
  implicit val impressionJsonFormat = jsonFormat8(Impression)
  implicit val bidRequestJsonFormat = jsonFormat5(BidRequest)

  implicit val bannerJsonFormat = jsonFormat4(Banner)
  implicit val bannerWithPriceJsonFormat = jsonFormat5(BannerWithPrice)
  implicit val bidResponseJsonFormat = jsonFormat4(BidResponse)
}
