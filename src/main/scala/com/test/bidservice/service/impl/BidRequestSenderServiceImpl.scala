package com.test.bidservice.service.impl

import akka.actor.typed.{ActorSystem, DispatcherSelector}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import com.test.bidservice.config.DspConfig
import com.test.bidservice.model.response.{BidderResponse, ResponseAggregationResult}
import com.test.bidservice.route.QueryParameters
import com.test.bidservice.service.BidRequestSenderService
import com.test.bidservice.util.{FutureUtil, JsonSupport, Logger}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success, Try}

class BidRequestSenderServiceImpl(price: Double)(implicit val system: ActorSystem[_])
  extends BidRequestSenderService
    with Logger
    with FutureUtil
    with JsonSupport{

  implicit val classicSystem = system.classicSystem
  implicit val ex = system.dispatchers.lookup(DispatcherSelector.blocking())
  implicit val mat = Materializer.createMaterializer(classicSystem)

  private[service] def safeFuture[T](future: Future[T]): Future[Try[T]] =
    future.map(Success(_)).recover { case e =>
      log.error("Exception processing future", e)
      Failure(e)
    }

  def withTimeout[T](future: Future[T], timeout: Duration)(implicit ec: ExecutionContext): Future[T] = {
    val prom = Promise[T]()
    val schedule = TimeoutScheduler.scheduleTimeout(prom, timeout)
    val combinedFut = Future.firstCompletedOf(List(future, prom.future))

    future.onComplete { _ => schedule.cancel() }

    combinedFut
  }

  private[service] def sendHttpRequestWithTimeout(
                                                   dsp: DspConfig,
                                                   price: Double
                                                 ): Future[(String, HttpResponse)] =
    withTimeout(
      Http()(classicSystem).singleRequest(
        HttpRequest(
          method = HttpMethods.POST,
          uri = Uri(dsp.host).withQuery(
            Query(
              (QueryParameters.Price, price.toString),
              (QueryParameters.Dsp, dsp.name)
            )
          ),
        )
      ),
      dsp.timeout.millis
    ).map(response => (dsp.name, response))

  override def sendBidRequest(dsps: Seq[DspConfig])
  : Future[ResponseAggregationResult] =
    Future.sequence(dsps.map { dsp =>
      safeFuture(sendHttpRequestWithTimeout(dsp, price))
    })
      .map(_.filter(_.isSuccess))
      .flatMap {
        responses =>
          responses.foldLeft(Future.successful(ResponseAggregationResult())) {
            case (agg, Success((dspName, response))) =>
              log.debug(
                s"Processing response from the following dsp: [$dspName], response: [${response.status}]"
              )
              response.status match {
                case StatusCodes.OK =>
                  val result = Unmarshal(response.entity).to[BidderResponse]
                  result.flatMap { bidResponse =>
                    agg.map(_.add(dspName, bidResponse))
                  }.recoverWith {
                    case ex: Exception =>
                      log.error("Error during bid response processing", ex)
                      agg
                  }
                case _ =>
                  agg
              }
          }
      }
}
