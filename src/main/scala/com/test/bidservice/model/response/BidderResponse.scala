package com.test.bidservice.model.response

case class BidderResponse(price: Double)

case class ResponseAggregationResult(result: Seq[(String, BidderResponse)] = Seq.empty) {
  def add(dspName: String, response: BidderResponse): ResponseAggregationResult =
    this.copy(result = (dspName, response) +: result)
}
