package com.filippodeluca.dataduggee

import java.time._
import scala.concurrent.duration.FiniteDuration

object model {

  case class Point(timestamp: Instant, value: Double)

  sealed trait Metric {
    def host: Option[String]
    def name: String
    def points: List[Point]
    def tags: Set[String]
  }

  case class Count(
      name: String,
      interval: FiniteDuration,
      points: List[Point],
      host: Option[String],
      tags: Set[String]
  ) extends Metric

  case class Gauge(
      name: String,
      points: List[Point],
      host: Option[String],
      tags: Set[String]
  ) extends Metric

  case class Rate(
      name: String,
      interval: FiniteDuration,
      points: List[Point],
      host: Option[String],
      tags: Set[String]
  ) extends Metric

}
