package com.test.bidservice.config

import com.typesafe.config.Config

case class DspConfig(name: String, host: String, timeout: Long)

object DspConfig {
  def fromConfig(config: Config): DspConfig = {
    DspConfig(
      config.getString("name"),
      config.getString("host"),
      config.getDuration("timeout").toMillis
    )
  }

}
