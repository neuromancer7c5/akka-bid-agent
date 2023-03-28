package com.test.bidservice.util

import com.typesafe.scalalogging.{Logger => ScalaLogger}

trait Logger {
  lazy val log: ScalaLogger = ScalaLogger(this.getClass)
}
