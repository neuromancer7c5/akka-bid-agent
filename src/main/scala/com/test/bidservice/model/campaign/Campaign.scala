package com.test.bidservice.model.campaign

case class Campaign(id: String,
                    country: String,
                    targeting: Targeting,
                    banners: List[Banner],
                    bid: Double)
