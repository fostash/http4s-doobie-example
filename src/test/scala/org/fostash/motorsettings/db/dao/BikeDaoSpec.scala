package org.fostash.motorsettings.db.dao

import cats.effect.IO
import doobie.scalatest.IOChecker
import doobie.util.transactor.Transactor
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import org.fostash.motorsettings.Config
import org.scalatest._

import scala.concurrent.ExecutionContext

class BikeDaoSpec extends WordSpec with Matchers with IOChecker {
  implicit val log = Slf4jLogger.unsafeCreate[IO]
  implicit val cs = IO.contextShift(ExecutionContext.global)

  val transactor = Transactor.fromDriverManager[IO](
    Config.DatabaseDriver, Config.DatabaseUrl, Config.DatabaseUsername, Config.DatabasePassword
  )

  "BikeDao" should {
    "read bike" in {

      check(BikeQueries.readBike(1L))
    }
  }

}
