package com.test.bidservice.service.impl

import com.test.bidservice.model.campaign.{Banner, Campaign}
import com.test.bidservice.model.request.{BidRequest, Impression}
import com.test.bidservice.service.BidRequestService

class BidRequestServiceImpl(campaigns: Seq[Campaign]) extends BidRequestService {

  override def processBid(bidRequest: BidRequest): Option[(String, Double, Banner)] = {
    getBidRequestCountry(bidRequest).flatMap { country =>
      campaigns
        .filter(_.country == country)
        .filter(_.targeting.targetedSiteIds.contains(bidRequest.site.id))
        .flatMap { campaign =>
          getBannersWithPrice(bidRequest, campaign)
            .map(bannerWithPrice =>
            (campaign.id.toString, bannerWithPrice._1, bannerWithPrice._2))
        }
        .headOption
    }
  }

  private [impl] def getBannersWithPrice(bidRequest: BidRequest,
                                         campaign: Campaign): List[(Double, Banner)] = {
    bidRequest.imp
      .map { impressions =>
        impressions.flatMap { impression =>
          val price = getPrice(impression.bidFloor.getOrElse(0.1d), campaign.bid)
          price
            .map { value =>
              val banners = getBanners(impression, campaign.banners)
              banners.map((value,_))
            }
            .getOrElse(List.empty)
        }
      }
      .getOrElse(List.empty)
  }


  private [impl] def getPrice(requestBid: Double,
                              campaignBid: Double): Option[Double] = {
    if (requestBid <= campaignBid) {
      Some(requestBid)
    } else {
      None
    }
  }

  private [impl] def getBanners(impression: Impression,
                                banners: List[Banner]): List[Banner] = {
    banners.filter(banner =>
      validateDimension(
        impression.h,
        impression.hmin,
        impression.hmax,
        banner.height
      ) &&
        validateDimension(
          impression.w,
          impression.wmin,
          impression.wmax,
          banner.width)
    )
  }

  private [impl] def getBidRequestCountry(bidRequest: BidRequest): Option[String] = {
    val country = for{
      device <-bidRequest.device
      deviceGeo <- device.geo
      deviceCountry <- deviceGeo.country
    } yield deviceCountry
    country.orElse {
      for {
        user <- bidRequest.user
        userGeo <- user.geo
        userCountry <- userGeo.country
      } yield userCountry
    }
  }
  private [impl] def validateDimension(requestValue: Option[Int],
                                       requestMinValue: Option[Int],
                                       requestMaxValue: Option[Int],
                                       bannerValue: Int): Boolean = {
    (requestValue, requestMinValue, requestMaxValue)  match {
      case (Some(value), _, _) => value == bannerValue
      case (_, Some(minValue), Some(maxValue)) =>
        minValue <= bannerValue && maxValue >= bannerValue
      case (_, Some(minValue), _) =>
        minValue <= bannerValue
      case (_, _, Some(maxValue)) =>
        maxValue >= bannerValue
      case _ =>
        false
    }
  }
}
