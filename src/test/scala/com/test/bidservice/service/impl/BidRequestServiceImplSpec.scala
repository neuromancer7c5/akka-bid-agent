package com.test.bidservice.service.impl

import com.test.bidservice.model.request._
import com.test.bidservice.util.CampaignHelper
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class BidRequestServiceImplSpec extends AnyWordSpecLike
  with Matchers{

  private val bidRequestServiceImpl = new BidRequestServiceImpl(CampaignHelper.getCampaigns)

  "validateDimension" should {
    "return true" when {
      "request value equals to banner value" in {

        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(350),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(bidFloor)
        )
        val bannerValue = 350
        bidRequestServiceImpl.validateDimension(
          requestValue = impression.w,
          requestMinValue = impression.wmin,
          requestMaxValue = impression.wmax,
          bannerValue = bannerValue
        ) should be(true)
      }

      "request value not passed and banner value in range of min and max values" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(400),
          w = None,
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(bidFloor)
        )
        val bannerValue = 350
        bidRequestServiceImpl.validateDimension(
          requestValue = impression.w,
          requestMinValue = impression.wmin,
          requestMaxValue = impression.wmax,
          bannerValue = bannerValue
        ) should be(true)
      }
    }
    "return false" when {
      "request value greater than banner value" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(300),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(bidFloor)
        )
        val bannerValue = 250
        bidRequestServiceImpl.validateDimension(
          requestValue = impression.w,
          requestMinValue = impression.wmin,
          requestMaxValue = impression.wmax,
          bannerValue = bannerValue
        ) should be(false)
      }

      "request value not passed and banner value in range of min and max values" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(300),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(bidFloor)
        )
        val bannerValue = 250
        bidRequestServiceImpl.validateDimension(
          requestValue = impression.w,
          requestMinValue = impression.wmin,
          requestMaxValue = impression.wmax,
          bannerValue = bannerValue
        ) should be(false)
      }
    }
  }

  "getBidRequestCountry" should {
    "return country" when {
      "device's geo country value is defined" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(300),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(bidFloor)
        )
        val site = Site(
          id = "0006a522ce0f4bbbbaa6b3c38cafaa0f",
          domain = "fake.tld"
        )
        val geo = Geo(
          country = Some("LT")
        )
        val bidUser = BidUser(
          id = "USARIO1",
          geo = Some(geo)
        )
        val device = Device(
          id = "440579f4b408831516ebd02f6e1c31b4",
          geo = Some(geo)
        )
        val requestId = "SGu1Jpq1IO"
        val bidRequest = BidRequest(
          id = requestId,
          imp = Some(List(impression)),
          site = site,
          user = Some(bidUser),
          device = Some(device)
        )

        bidRequestServiceImpl.getBidRequestCountry(bidRequest) should be(geo.country)
      }
      "user's geo country value is defined and device's geo country is not" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(300),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(bidFloor)
        )
        val site = Site(
          id = "0006a522ce0f4bbbbaa6b3c38cafaa0f",
          domain = "fake.tld"
        )
        val geo = Geo(
          country = Some("LT")
        )
        val bidUser = BidUser(
          id = "USARIO1",
          geo = Some(geo)
        )
        val device = Device(
          id = "440579f4b408831516ebd02f6e1c31b4",
          geo = Some(geo)
        )
        val requestId = "SGu1Jpq1IO"
        val bidRequest = BidRequest(
          id = requestId,
          imp = Some(List(impression)),
          site = site,
          user = Some(bidUser),
          device = None
        )

        bidRequestServiceImpl.getBidRequestCountry(bidRequest) should be(geo.country)
      }
    }
    "return false" when {
      "geo country value is not defined" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(300),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(bidFloor)
        )
        val site = Site(
          id = "0006a522ce0f4bbbbaa6b3c38cafaa0f",
          domain = "fake.tld"
        )
        val geo = Geo(
          country = None
        )
        val bidUser = BidUser(
          id = "USARIO1",
          geo = Some(geo)
        )
        val device = Device(
          id = "440579f4b408831516ebd02f6e1c31b4",
          geo = Some(geo)
        )
        val requestId = "SGu1Jpq1IO"
        val bidRequest = BidRequest(
          id = requestId,
          imp = Some(List(impression)),
          site = site,
          user = Some(bidUser),
          device = Some(device)
        )

        bidRequestServiceImpl.getBidRequestCountry(bidRequest) should be(None)
      }
      "geo is not defined" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(300),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(bidFloor)
        )
        val site = Site(
          id = "0006a522ce0f4bbbbaa6b3c38cafaa0f",
          domain = "fake.tld"
        )
        val bidUser = BidUser(
          id = "USARIO1",
          geo = None
        )
        val device = Device(
          id = "440579f4b408831516ebd02f6e1c31b4",
          geo = None
        )
        val requestId = "SGu1Jpq1IO"
        val bidRequest = BidRequest(
          id = requestId,
          imp = Some(List(impression)),
          site = site,
          user = Some(bidUser),
          device = Some(device)
        )

        bidRequestServiceImpl.getBidRequestCountry(bidRequest) should be(None)
      }
      "device and user is not defined" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(300),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(bidFloor)
        )
        val site = Site(
          id = "0006a522ce0f4bbbbaa6b3c38cafaa0f",
          domain = "fake.tld"
        )
        val requestId = "SGu1Jpq1IO"
        val bidRequest = BidRequest(
          id = requestId,
          imp = Some(List(impression)),
          site = site,
          user = None,
          device = None
        )

        bidRequestServiceImpl.getBidRequestCountry(bidRequest) should be(None)
      }
    }
  }

  "getBanners" should {
    "return list of banners" when {
      "it fits the size" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(300),
          wmax = Some(700),
          w = None,
          hmin = Some(250),
          hmax = Some(400),
          h = None,
          bidFloor = Some(bidFloor)
        )
        val campaignBanners = CampaignHelper.getCampaigns(2).banners
        bidRequestServiceImpl.getBanners(impression, campaignBanners) should be (campaignBanners)
      }
    }
    "return list with single banner" when {
      "not all of the list's banners fit the size" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(700),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(400),
          bidFloor = Some(bidFloor)
        )
        val campaignBanners = CampaignHelper.getCampaigns(2).banners
        val firstBanner = CampaignHelper.getCampaigns(2).banners.head
        bidRequestServiceImpl.getBanners(impression, campaignBanners) should be (List(firstBanner))
      }
    }

    "return empty list" when {
      "none of the list's banners fit the size" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(800),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(300),
          bidFloor = Some(bidFloor)
        )
        val campaignBanners = CampaignHelper.getCampaigns(2).banners
        bidRequestServiceImpl.getBanners(impression, campaignBanners) should be (List.empty)
      }
    }
  }

  "getPrice" should{
    "return price" when{
      "campaign bid is greater then request bid" in {
        val bidFloor = 3.12123
        val campaignBid = CampaignHelper.getCampaigns.head.bid
        bidRequestServiceImpl.getPrice(bidFloor, campaignBid) should be (Some(bidFloor))
      }
      "campaign bid and request bid are equals" in {
        val campaignBid = CampaignHelper.getCampaigns(2).bid
        val bidFloor = CampaignHelper.getCampaigns(2).bid
        bidRequestServiceImpl.getPrice(bidFloor, campaignBid) should be (Some(bidFloor))
      }
    }
    "return none" when{
      "campaign bid is less then request bid" in {
        val bidFloor = 3.12123
        val campaignBid = CampaignHelper.getCampaigns(1).bid
        bidRequestServiceImpl.getPrice(bidFloor, campaignBid) should be (None)
      }
    }
  }

  "getBannersWithPrice" should {
    "return list of banners" when {
      "it fits the size for one of the impressions" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(300),
          wmax = Some(700),
          w = None,
          hmin = Some(250),
          hmax = Some(400),
          h = None,
          bidFloor = Some(bidFloor)
        )
        val site = Site(
          id = "0006a522ce0f4bbbbaa6b3c38cafaa0f",
          domain = "fake.tld"
        )
        val geo = Geo(
          country = Some("LT")
        )
        val bidUser = BidUser(
          id = "USARIO1",
          geo = Some(geo)
        )
        val device = Device(
          id = "440579f4b408831516ebd02f6e1c31b4",
          geo = Some(geo)
        )
        val requestId = "SGu1Jpq1IO"
        val bidRequest = BidRequest(
          id = requestId,
          imp = Some(List(impression)),
          site = site,
          user = Some(bidUser),
          device = Some(device)
        )
        val campaign = CampaignHelper.getCampaigns(2)

        val expectedBannersWithPrice = List(
          (campaign.id.toString, bidFloor, CampaignHelper.getCampaigns(2).banners.head),
          (campaign.id.toString, bidFloor, CampaignHelper.getCampaigns(2).banners(1))
        )
        bidRequestServiceImpl.getBannersWithPrice(bidRequest, campaign) should be (expectedBannersWithPrice)
      }
      "it fits the size for each impression" in {
        val firstBidFloor = 3.12123
        val firstImpression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(700),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(400),
          bidFloor = Some(firstBidFloor)
        )
        val secondBidFloor = 2.55
        val secondImpression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(300),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(secondBidFloor)
        )
        val site = Site(
          id = "0006a522ce0f4bbbbaa6b3c38cafaa0f",
          domain = "fake.tld"
        )
        val geo = Geo(
          country = Some("LT")
        )
        val bidUser = BidUser(
          id = "USARIO1",
          geo = Some(geo)
        )
        val device = Device(
          id = "440579f4b408831516ebd02f6e1c31b4",
          geo = Some(geo)
        )
        val requestId = "SGu1Jpq1IO"
        val bidRequest = BidRequest(
          id = requestId,
          imp = Some(List(firstImpression, secondImpression)),
          site = site,
          user = Some(bidUser),
          device = Some(device)
        )
        val campaign = CampaignHelper.getCampaigns(2)

        val expectedBannersWithPrice = List(
          (campaign.id.toString, firstBidFloor, CampaignHelper.getCampaigns(2).banners.head),
          (campaign.id.toString, secondBidFloor, CampaignHelper.getCampaigns(2).banners(1))
        )
        bidRequestServiceImpl.getBannersWithPrice(bidRequest, campaign) should be (expectedBannersWithPrice)
      }
    }
    "return list with single banner" when {
      "not all of the list's banners fit the size" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(700),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(400),
          bidFloor = Some(bidFloor)
        )
        val site = Site(
          id = "0006a522ce0f4bbbbaa6b3c38cafaa0f",
          domain = "fake.tld"
        )
        val geo = Geo(
          country = Some("LT")
        )
        val bidUser = BidUser(
          id = "USARIO1",
          geo = Some(geo)
        )
        val device = Device(
          id = "440579f4b408831516ebd02f6e1c31b4",
          geo = Some(geo)
        )
        val requestId = "SGu1Jpq1IO"
        val bidRequest = BidRequest(
          id = requestId,
          imp = Some(List(impression)),
          site = site,
          user = Some(bidUser),
          device = Some(device)
        )
        val campaign = CampaignHelper.getCampaigns(2)

        val expectedBannersWithPrice = List(
          (campaign.id.toString, bidFloor, CampaignHelper.getCampaigns(2).banners.head)
        )
        bidRequestServiceImpl.getBannersWithPrice(bidRequest, campaign) should be(expectedBannersWithPrice)
      }
    }
    "return empty list" when {
      "none of the campaign's banners fit the size" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(800),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(bidFloor)
        )
        val site = Site(
          id = "0006a522ce0f4bbbbaa6b3c38cafaa0f",
          domain = "fake.tld"
        )
        val geo = Geo(
          country = Some("LT")
        )
        val bidUser = BidUser(
          id = "USARIO1",
          geo = Some(geo)
        )
        val device = Device(
          id = "440579f4b408831516ebd02f6e1c31b4",
          geo = Some(geo)
        )
        val requestId = "SGu1Jpq1IO"
        val bidRequest = BidRequest(
          id = requestId,
          imp = Some(List(impression)),
          site = site,
          user = Some(bidUser),
          device = Some(device)
        )
        val campaign = CampaignHelper.getCampaigns(2)
        val expectedBannersWithPrice = List.empty
        bidRequestServiceImpl.getBannersWithPrice(bidRequest, campaign) should be(expectedBannersWithPrice)
      }
    }
  }
  "processBid" should {
    "return tuple of campaign id and list of BannerWithPrice" when {
      "request bid matches with campaign" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(300),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(bidFloor)
        )
        val site = Site(
          id = "0006a522ce0f4bbbbaa6b3c38cafaa0f",
          domain = "fake.tld"
        )
        val geo = Geo(
          country = Some("LT")
        )
        val bidUser = BidUser(
          id = "USARIO1",
          geo = Some(geo)
        )
        val device = Device(
          id = "440579f4b408831516ebd02f6e1c31b4",
          geo = Some(geo)
        )
        val requestId = "SGu1Jpq1IO"
        val bidRequest = BidRequest(
          id = requestId,
          imp = Some(List(impression)),
          site = site,
          user = Some(bidUser),
          device = Some(device)
        )

        val expectedBanner = CampaignHelper.getCampaigns(2).banners(1)

        val expectedCampaignId = CampaignHelper.getCampaigns(2).id.toString
        bidRequestServiceImpl.processBid(bidRequest) should be (Some((expectedCampaignId, bidFloor, expectedBanner)))
      }
    }
    "return None" when {
      "request bid not matches with any campaign by country" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(300),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(bidFloor)
        )
        val site = Site(
          id = "0006a522ce0f4bbbbaa6b3c38cafaa0f",
          domain = "fake.tld"
        )
        val geo = Geo(
          country = Some("RU")
        )
        val bidUser = BidUser(
          id = "USARIO1",
          geo = Some(geo)
        )
        val device = Device(
          id = "440579f4b408831516ebd02f6e1c31b4",
          geo = Some(geo)
        )
        val requestId = "SGu1Jpq1IO"
        val bidRequest = BidRequest(
          id = requestId,
          imp = Some(List(impression)),
          site = site,
          user = Some(bidUser),
          device = Some(device)
        )

        bidRequestServiceImpl.processBid(bidRequest) should be (None)
      }
      "request bid not matches with any campaign by bidFloor" in {
        val bidFloor = 4.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(300),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(bidFloor)
        )
        val site = Site(
          id = "0006a522ce0f4bbbbaa6b3c38cafaa0f",
          domain = "fake.tld"
        )
        val geo = Geo(
          country = Some("LT")
        )
        val bidUser = BidUser(
          id = "USARIO1",
          geo = Some(geo)
        )
        val device = Device(
          id = "440579f4b408831516ebd02f6e1c31b4",
          geo = Some(geo)
        )
        val requestId = "SGu1Jpq1IO"
        val bidRequest = BidRequest(
          id = requestId,
          imp = Some(List(impression)),
          site = site,
          user = Some(bidUser),
          device = Some(device)
        )

        bidRequestServiceImpl.processBid(bidRequest) should be (None)
      }
      "request bid not matches with any campaign by site id" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(300),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(bidFloor)
        )
        val site = Site(
          id = "5acba17038bf17a24539143444e97750",
          domain = "fake.tld"
        )
        val geo = Geo(
          country = Some("LT")
        )
        val bidUser = BidUser(
          id = "USARIO1",
          geo = Some(geo)
        )
        val device = Device(
          id = "440579f4b408831516ebd02f6e1c31b4",
          geo = Some(geo)
        )
        val requestId = "SGu1Jpq1IO"
        val bidRequest = BidRequest(
          id = requestId,
          imp = Some(List(impression)),
          site = site,
          user = Some(bidUser),
          device = Some(device)
        )

        bidRequestServiceImpl.processBid(bidRequest) should be (None)
      }
      "request bid not matches with any campaign by banner size" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(800),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(bidFloor)
        )
        val site = Site(
          id = "0006a522ce0f4bbbbaa6b3c38cafaa0f",
          domain = "fake.tld"
        )
        val geo = Geo(
          country = Some("LT")
        )
        val bidUser = BidUser(
          id = "USARIO1",
          geo = Some(geo)
        )
        val device = Device(
          id = "440579f4b408831516ebd02f6e1c31b4",
          geo = Some(geo)
        )
        val requestId = "SGu1Jpq1IO"
        val bidRequest = BidRequest(
          id = requestId,
          imp = Some(List(impression)),
          site = site,
          user = Some(bidUser),
          device = Some(device)
        )

        bidRequestServiceImpl.processBid(bidRequest) should be (None)
      }
    }
  }
}
