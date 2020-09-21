package com.test.bidservice

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import com.test.bidservice.actor.BidActor
import com.test.bidservice.actor.BidActor.{GetBidResponse, ProcessBid}
import com.test.bidservice.model.campaign.Banner
import com.test.bidservice.model.request._
import com.test.bidservice.model.response.{BannerWithPrice, BidResponse}
import com.test.bidservice.service.BidRequestService
import org.mockito.MockitoSugar
import org.scalatest.wordspec.AnyWordSpecLike


class BidActorSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike  with MockitoSugar {

  val bidRequestValidatorMock = mock[BidRequestService]

  "A BidActor" must {
    "reply with bid response" in {
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
      val adId = "1"
      val banner = Banner(
        id = 1,
        src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
        width = 300,
        height = 250
      )
      val getBidResponse = GetBidResponse(
        Some(BidResponse(
          id = "response1",
          bidRequestId = requestId,
          price = bidFloor,
          adId = Some(adId),
          banner = List(banner)
        ))
      )
      val replyProbe = createTestProbe[GetBidResponse]()
      val underTest = spawn(BidActor(bidRequestValidatorMock))
      when(bidRequestValidatorMock.processBid(bidRequest)).thenReturn(Some((adId, bidFloor, List(banner))))
      underTest ! ProcessBid(bidRequest, replyProbe.ref)
      replyProbe.expectMessage(getBidResponse)
    }

    "reply with empty response" in {
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
      val getBidResponse = GetBidResponse(None)
      val replyProbe = createTestProbe[GetBidResponse]()
      val underTest = spawn(actor.BidActor(bidRequestValidatorMock))
      when(bidRequestValidatorMock.processBid(bidRequest)).thenReturn(None)
      underTest ! ProcessBid(bidRequest, replyProbe.ref)
      replyProbe.expectMessage(getBidResponse)
    }

  }

}
