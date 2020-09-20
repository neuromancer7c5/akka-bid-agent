package com.test.bidservice.util

import com.test.bidservice.model.campaign.{Banner, Campaign, Targeting}
import com.test.bidservice.model.campaign.Campaign

object CampaignHelper {
  def getCampaigns: Seq[Campaign] = {
    val firstCampaign = Campaign(
      id = "1",
      country = "US",
      targeting = Targeting(
        targetedSiteIds = LazyList(
          "cba08101646ca96430f0a3f3f0bf47d7",
          "nb24fb2304680a03ed98da403d7829530",
          "nfc045933fc6d70e03239f0ffa0e8f831",
          "neee9abeedce7ca274c59905140b207d7",
          "nb3f564aaf717e692166c270a9656984a",
          "n770f68f97ac53b6d1777e2f29f25e07c",
          "nd33fed44786509b69f01f468a98f601c",
          "nb248211dd4073c895d42fbca151bd6ad",
          "n1d5d05e2c8eb5eda09d27d5f2f933225",
          "nf5a3aa702ec88789a10478b8f08a10c4"
        )
      ),
      banners = List(Banner(
        id = 1   ,
        src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
        width = 300,
        height = 250
      ),
        Banner(
          id = 2,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph2.jpeg",
          width = 150,
          height = 100
        )
      ),
      bid = 5d
    )

    val secondCampaign = Campaign(
      id = "2",
      country = "GB",
      targeting = Targeting(
        targetedSiteIds = LazyList(
          "39047682742541b5bfe026316aecc29c",
          "d02dc29c6bf9dbbefaff79d62941fd7c",
          "5acba17038bf17a24539143444e97750",
          "ac33d2783644466aed64fc2c944a9c20",
          "17b5d323552e7542985a20aa9256b851",
          "c2465f024f9673d068ceb2947a1ecc5a",
          "8d889394436d55eeeefcfa2b3d542bad",
          "38f8732b9d93537d2a49f10cf46f2832",
          "04cc4dbff0b7c974911b70806a8a1a02",
          "0b42ee7e1754c4017d8578d251d0c727"
        )
      ),
      banners = List(Banner(
        id = 3,
        src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph3.jpeg",
        width = 500,
        height = 350
      ),
        Banner(
          id = 4,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph4.jpeg",
          width = 330,
          height = 170
        )
      ),
      bid = 3.33d
    )

    val thirdCampaign = Campaign(
      id = "3",
      country = "LT",
      targeting = Targeting(
        targetedSiteIds = LazyList(
          "0006a522ce0f4bbbbaa6b3c38cafaa0f",
          "28b9993c0c297ebfd7d27fc32a5b27cf",
          "412970ee2cf5b321f719f49017e61a1",
          "c640966b8f5e4573ec6d6e89f7de170c",
          "c5bb3f028eecbbfa54c2503f26c88864",
          "17eaa949c9a1283208238a437c0e2f96",
          "d7eed1aee20890e7ee93fa9471cfadd9",
          "80e18eb71c3fe17051bd002801252e0a",
          "b7029683ae2083484284d47418f7c543",
          "bf6cbaacf73fc7579ebda11a62af4367"
        )
      ),
      banners = List(Banner(
        id = 5,
        src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph5.jpeg",
        width = 700,
        height = 400
      ),
        Banner(
          id = 6,
          src = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph6.jpeg",
          width = 300,
          height = 250
        )
      ),
      bid = 3.05d
    )

    Seq(
      firstCampaign,
      secondCampaign,
      thirdCampaign
    )
  }
}
