package com.filippodeluca.dataduggee

import java.time.Instant
import scala.concurrent.duration._

import org.scalacheck.Gen
import org.scalacheck.Arbitrary

import model._

object arbitraries {
  import Gen._
  import Arbitrary.arbitrary

  implicit def genToArb[A: Gen]: Arbitrary[A] = Arbitrary(implicitly[Gen[A]])

  val genTag: Gen[Tag] = for {
    n <- choose(0, 3)
    tagName = s"phil-dataduggee-tag-$n"
    tagValue = s"dataduggee-tag-after-colon-$n"
  } yield Tag(tagName, Some("blah"))

  // Generate a instant between now and 30minutes ago
  implicit lazy val genInstant: Gen[Instant] = for {
    now <- Instant.now()
    adj <- choose(0, 30.minutes.toMillis)
  } yield now.minusMillis(adj)

  implicit lazy val genFiniteDuration: Gen[FiniteDuration] = for {
    millis <- choose(0, 60000)
  } yield millis.milliseconds

  implicit lazy val genPoint: Gen[Point] = for {
    timestamp <- arbitrary[Instant]
    value <- arbitrary[Float]
  } yield Point(timestamp, value)

  implicit lazy val genGauge: Gen[Gauge] = for {
    noOfPoints <- chooseNum(1, 100)
    points <- listOfN(noOfPoints, genPoint)
    noOfTags <- chooseNum(0, 3)
    tags <- listOfN(noOfTags, genTag).map(_.toSet)
  } yield Gauge(
    name = "phil.dataduggee.test-gauge",
    points = points,
    host = None,
    tags = tags
  )

  implicit lazy val genCount: Gen[Count] = for {
    noOfPoints <- chooseNum(1, 100)
    points <- listOfN(noOfPoints, genPoint)
    interval <- arbitrary[FiniteDuration]
    noOfTags <- chooseNum(0, 3)
    tags <- listOfN(noOfTags, genTag).map(_.toSet)
  } yield Count(
    name = "phil.dataduggee.test-count",
    points = points,
    interval = interval,
    host = None,
    tags = tags
  )

  implicit lazy val genRate: Gen[Rate] = for {
    noOfPoints <- chooseNum(1, 100)
    points <- listOfN(noOfPoints, genPoint)
    interval <- arbitrary[FiniteDuration]
    noOfTags <- chooseNum(0, 3)
    tags <- listOfN(noOfTags, genTag).map(_.toSet)
  } yield Rate(
    name = "phil.dataduggee.test-rate",
    points = points,
    interval = interval,
    host = None,
    tags = tags
  )

  implicit lazy val genMetric: Gen[Metric] = oneOf(
    genCount, genGauge, genRate
  )

}
