package com.test.bidservice.service.impl

import com.test.bidservice.model.campaign.{Banner, Campaign}
import com.test.bidservice.model.request.{BidRequest, Impression}
import com.test.bidservice.service.BidRequestService

class BidRequestServiceImpl(campaigns: Seq[Campaign]) extends BidRequestService {

  override def processBid(bidRequest: BidRequest): Option[(String, Double, Banner)] = {
    getBidRequestCountry(bidRequest).flatMap { country =>
      campaigns
        .filter(_.country.equals(country))
        .filter(_.targeting.targetedSiteIds.contains(bidRequest.site.id))
        .flatMap { campaign =>
          getBannersWithPrice(bidRequest, campaign).map(bannerWithPrice =>
            (campaign.id.toString, bannerWithPrice._1, bannerWithPrice._2))
        }.headOption
    }
  }

  def getBannersWithPrice(bidRequest: BidRequest,
                          campaign: Campaign): List[(Double, Banner)] = {
    bidRequest.imp.map { impressions =>
      impressions.flatMap { impression =>
        val price = getPrice(impression.bidFloor.getOrElse(0.1d), campaign.bid)
        price.map { value =>
          val banners = getBanners(impression, campaign.banners)
          banners.map((value,_))
        }.getOrElse(List.empty)
      }
    }.getOrElse(List.empty)
  }


  def getPrice(requestBid: Double,
               campaignBid: Double): Option[Double] = {
    if (requestBid <= campaignBid) {
      Some(requestBid)
    } else {
      None
    }
  }

  def getBanners(impression: Impression,
                 banners: List[Banner]): List[Banner] = {
    banners.filter(banner =>
      compareDimension(
        impression.h,
        impression.hmin,
        impression.hmax,
        banner.height
      ) &&
        compareDimension(
          impression.w,
          impression.wmin,
          impression.wmax,
          banner.width)
    )
  }

  def getBidRequestCountry(bidRequest: BidRequest): Option[String] = {
    bidRequest.device.flatMap(device =>
      device.geo.flatMap { geo =>
        geo.country
      }
    )
  }
  def compareDimension(requestValue: Option[Int],
                       requestMinValue: Option[Int],
                       requestMaxValue: Option[Int],
                       bannerValue: Int): Boolean = {
    (requestValue, requestMinValue, requestMaxValue)  match {
      case (Some(value), _, _) => value == bannerValue
      case (None, Some(minValue), Some(maxValue)) =>
        val requestRange = Range.inclusive(
          minValue,
          maxValue
        )
        requestRange.contains(bannerValue)
      case (None, Some(minValue), None) =>
        minValue <= bannerValue
      case (None, None, Some(maxValue)) =>
        maxValue >= bannerValue
      case (None, None, None) =>
        false
    }
  }
}
