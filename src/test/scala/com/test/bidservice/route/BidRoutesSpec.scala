package com.test.bidservice.route

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.test.bidservice.actor.BidActor
import com.test.bidservice.model.campaign.Banner
import com.test.bidservice.model.request._
import com.test.bidservice.service.BidRequestService
import com.test.bidservice.util.{JsonMatcher, JsonSupport}
import org.json4s.{DefaultFormats, Formats}
import org.mockito.MockitoSugar
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class BidRoutesSpec extends AnyWordSpecLike
  with Matchers
  with ScalaFutures
  with ScalatestRouteTest
  with MockitoSugar
  with JsonMatcher
  with JsonSupport{

  override implicit def jsonFormat: Formats = DefaultFormats

  lazy val testKit = ActorTestKit()
  implicit def typedSystem = testKit.system
  override def createActorSystem(): akka.actor.ActorSystem =
    testKit.system.toClassic

  val bidRequestValidatorMock = mock[BidRequestService]
  val userRegistry = testKit.spawn(BidActor(bidRequestValidatorMock))
  lazy val routes = new BidRoutes(userRegistry).bidRoutes

  "POST" should {
    "return 200 and response body" in {
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

      val banner = Banner(
        id = 1,
        src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
        width = 300,
        height = 250
      )

      val bidEntity = Marshal(bidRequest).to[MessageEntity].futureValue
      when(bidRequestValidatorMock.processBid(bidRequest)).thenReturn(Some(("1", bidFloor, banner)))

      val request = Post("/bids").withEntity(bidEntity)

      request ~> routes ~> check {
        status should be (StatusCodes.OK)

        contentType should be (ContentTypes.`application/json`)

        entityAs[String] should haveJson("""
                                           |{
                                           |"id": "response1",
                                           |"bidRequestId": "SGu1Jpq1IO",
                                           |"price": 3.12123,
                                           |"adId": "1",
                                           |"banner":
                                           |{
                                           |  "id": 1,
                                           |  "height": 250,
                                           |  "width": 300,
                                           |  "src": "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg"
                                           |}
                                           |}"""
          .stripMargin
        )

      }
    }
    "return 204 and no response body" in {
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
      val bidEntity = Marshal(bidRequest).to[MessageEntity].futureValue
      when(bidRequestValidatorMock.processBid(bidRequest)).thenReturn(None)

      val request = Post("/bids").withEntity(bidEntity)

      request ~> routes ~> check {
        status should be (StatusCodes.NoContent)

        entityAs[String] shouldEqual ""

      }
    }
  }
}
