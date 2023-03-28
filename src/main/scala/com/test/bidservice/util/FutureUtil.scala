package com.test.bidservice.util

import io.netty.util.{HashedWheelTimer, Timeout, TimerTask}

import java.util.concurrent.TimeUnit
import scala.concurrent.Promise
import scala.concurrent.duration.Duration
import scala.util.control.NoStackTrace

trait FutureUtil {
  object TimeoutScheduler {
    private val timer = new HashedWheelTimer(2, TimeUnit.MILLISECONDS)

    def scheduleTimeout(promise: Promise[_], after: Duration) = {
      timer.newTimeout(
        new TimerTask {
          def run(timeout: Timeout) {
            promise.failure(new FutureTimeoutException())
          }
        },
        after.toNanos,
        TimeUnit.NANOSECONDS
      )
    }
  }

  class FutureTimeoutException extends Exception with NoStackTrace
}
