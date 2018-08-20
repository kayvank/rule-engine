package com.zendesk.utils

import com.typesafe.config.ConfigFactory
object Globals {
  val cfg = ConfigFactory.load
  val zenCfg = cfg.getConfig("zendesk")
}
