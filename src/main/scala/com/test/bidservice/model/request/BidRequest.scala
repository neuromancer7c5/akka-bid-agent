package com.test.bidservice.model.request

case class BidRequest(id: String,
                      imp: Option[List[Impression]],
                      site: Site,
                      user: Option[BidUser],
                      device: Option[Device])