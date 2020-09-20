package com.test.bidservice.service.impl

import com.test.bidservice.model.campaign.{Banner, Campaign}
import com.test.bidservice.model.request.{BidRequest, Impression}
import com.test.bidservice.model.response.{BannerWithPrice, BannerWithPriceBuilder}
import com.test.bidservice.service.BidRequestService
import com.test.bidservice.model.campaign.Campaign
import com.test.bidservice.model.request.Impression
import com.test.bidservice.model.response.BannerWithPriceBuilder

class BidRequestServiceImpl(campaigns: Seq[Campaign]) extends BidRequestService {

  override def processBid(bidRequest: BidRequest): Option[(String, List[BannerWithPrice])] = {
    getBidRequestCountry(bidRequest).flatMap { country =>
      campaigns.find(_.country.equals(country)).flatMap { campaign =>
        if (compareSiteId(bidRequest.site.id, campaign.targeting.targetedSiteIds)) {
          getBannersWithPrice(bidRequest, campaign)
            .filterNot(_.isEmpty)
            .map{banners =>
              (campaign.id, banners)}
        } else {
          None
        }
      }
    }
  }

  def getBannersWithPrice(bidRequest: BidRequest,
                          campaign: Campaign): Option[List[BannerWithPrice]] = {
    bidRequest.imp.map { impressions =>
      impressions.flatMap { impression =>
        val price = getPrice(impression.bidFloor.getOrElse(0.1d), campaign.bid)
        price match {
          case None => None
          case Some(value) =>
            val banners = getBanners(impression, campaign.banners)
            banners.map(banner => BannerWithPriceBuilder(banner, value))
        }
      }
    }
  }


  def getPrice(requestBid: Double,
               campaignBid: Double): Option[Double] = {
    if (requestBid >= campaignBid) {
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

  def compareSiteId(siteId: String,
                    targetedSiteIds: LazyList[String]): Boolean = {
    targetedSiteIds.exists(_.equals(siteId))
  }

  def getBidRequestCountry(bidRequest: BidRequest): Option[String] = {
    bidRequest.user.flatMap(user =>
      user.geo.flatMap { geo =>
        geo.country
      }
    )
  }
  def compareDimension(requestValue: Option[Int],
                       requestMinValue: Option[Int],
                       requestMaxValue: Option[Int],
                       bannerValue: Int): Boolean = {
    requestValue match {
      case Some(value) =>
        if (value <= bannerValue) {
          true
        } else {
          false
        }
      case None =>
        val requestRange = Range.inclusive(
          requestMinValue.getOrElse(0),
          requestMaxValue.getOrElse(0)
        )
        requestRange.contains(bannerValue)
    }
  }
}
