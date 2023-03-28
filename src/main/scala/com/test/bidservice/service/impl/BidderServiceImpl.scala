package com.test.bidservice.service.impl

import com.test.bidservice.model.response.BidderResponse
import com.test.bidservice.service.BidderService

import scala.concurrent.Future
import scala.util.Random

class BidderServiceImpl extends BidderService{
  override def handleBid(price: Double): BidderResponse = {
    val randomSleep = Random.between(10 , 100)
    val randomPrice = Random.nextDouble().floor + price
    Thread.sleep(randomSleep)
    BidderResponse(randomPrice)
  }
}
