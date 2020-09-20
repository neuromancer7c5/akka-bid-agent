package com.test.bidservice

import com.test.bidservice.model.request.{BidRequest, BidUser, Device, Geo, Impression, Site}
import com.test.bidservice.model.response.BannerWithPriceBuilder
import com.test.bidservice.service.impl.BidRequestServiceImpl
import com.test.bidservice.util.CampaignHelper
import com.test.bidservice.model.request.Device
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class BidRequestServiceImplSpec extends AnyWordSpecLike
  with Matchers{

  private val bidRequestServiceImpl = new BidRequestServiceImpl(CampaignHelper.getCampaigns)

  "compareDimension" should {
    "return true" when {
      "request value less than banner value" in {

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
        val bannerValue = 350
        bidRequestServiceImpl.compareDimension(
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
          wmax = Some(300),
          w = Some(300),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(bidFloor)
        )
        val bannerValue = 350
        bidRequestServiceImpl.compareDimension(
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
        bidRequestServiceImpl.compareDimension(
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
        bidRequestServiceImpl.compareDimension(
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
      "user's geo country value is defined" in {
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
    }
    "return false" when {
      "user's geo country value is not defined" in {
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
      "user's geo is not defined" in {
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
      "user is not defined" in {
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
        val device = Device(
          id = "440579f4b408831516ebd02f6e1c31b4",
          geo = None
        )
        val requestId = "SGu1Jpq1IO"
        val bidRequest = BidRequest(
          id = requestId,
          imp = Some(List(impression)),
          site = site,
          user = None,
          device = Some(device)
        )

        bidRequestServiceImpl.getBidRequestCountry(bidRequest) should be(None)
      }
    }
  }
  "compareSiteId" should {
    "return true" when {
      "campaign contains requested site id" in {
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
        val campaignSiteIds = CampaignHelper.getCampaigns(2).targeting.targetedSiteIds
        bidRequestServiceImpl.compareSiteId(bidRequest.site.id, campaignSiteIds) should be (true)
      }
    }
    "return false" when {
      "campaign doesn't contain requested site id" in {
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
        val campaignSiteIds = CampaignHelper.getCampaigns.head.targeting.targetedSiteIds
        bidRequestServiceImpl.compareSiteId(bidRequest.site.id, campaignSiteIds) should be (false)
      }
    }
  }

  "getBanners" should {
    "return list of banners" when {
      "it fits the size" in {
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
          w = Some(350),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(300),
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
      "campaign bid is less then request bid" in {
        val bidFloor = 3.12123
        val campaignBid = CampaignHelper.getCampaigns(2).bid
        bidRequestServiceImpl.getPrice(bidFloor, campaignBid) should be (Some(bidFloor))
      }
      "campaign bid and request bid are equals" in {
        val campaignBid = CampaignHelper.getCampaigns(2).bid
        val bidFloor = CampaignHelper.getCampaigns(2).bid
        bidRequestServiceImpl.getPrice(bidFloor, campaignBid) should be (Some(bidFloor))
      }
    }
    "return none" when{
      "campaign bid is greater then request bid" in {
        val bidFloor = 3.12123
        val campaignBid = CampaignHelper.getCampaigns.head.bid
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
        val campaign = CampaignHelper.getCampaigns(2)

        val expectedBannersWithPrice = List(
          BannerWithPriceBuilder(
            CampaignHelper.getCampaigns(2).banners.head,
            bidFloor),
          BannerWithPriceBuilder(
            CampaignHelper.getCampaigns(2).banners(1),
            bidFloor)
        )
        bidRequestServiceImpl.getBannersWithPrice(bidRequest, campaign) should be (Some(expectedBannersWithPrice))
      }
      "it fits the size for each impression" in {
        val firstBidFloor = 3.12123
        val firstImpression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(400),
          hmin = Some(100),
          hmax = Some(300),
          h = Some(250),
          bidFloor = Some(firstBidFloor)
        )
        val secondBidFloor = 3.55
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
          BannerWithPriceBuilder(
            CampaignHelper.getCampaigns(2).banners.head,
            firstBidFloor),
          BannerWithPriceBuilder(
            CampaignHelper.getCampaigns(2).banners.head,
            secondBidFloor),
          BannerWithPriceBuilder(
            CampaignHelper.getCampaigns(2).banners(1),
            secondBidFloor)
        )
        bidRequestServiceImpl.getBannersWithPrice(bidRequest, campaign) should be (Some(expectedBannersWithPrice))
      }
    }
    "return list with single banner" when {
      "not all of the list's banners fit the size" in {
        val bidFloor = 3.12123
        val impression = Impression(
          id = "1",
          wmin = Some(50),
          wmax = Some(300),
          w = Some(400),
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

        val expectedBannersWithPrice = List(
          BannerWithPriceBuilder(
            CampaignHelper.getCampaigns(2).banners.head,
            bidFloor)
        )
        bidRequestServiceImpl.getBannersWithPrice(bidRequest, campaign) should be(Some(expectedBannersWithPrice))
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
        bidRequestServiceImpl.getBannersWithPrice(bidRequest, campaign) should be(Some(expectedBannersWithPrice))
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

        val expectedBannersWithPrice = List(
          BannerWithPriceBuilder(
            CampaignHelper.getCampaigns(2).banners.head,
            bidFloor),
          BannerWithPriceBuilder(
            CampaignHelper.getCampaigns(2).banners(1),
            bidFloor)
        )
        val expectedCampaignId = CampaignHelper.getCampaigns(2).id
        bidRequestServiceImpl.processBid(bidRequest) should be (Some((expectedCampaignId,expectedBannersWithPrice)))
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
        val bidFloor = 2.12123
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
