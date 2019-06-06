package org.fostash.motorsettings

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import org.fostash.motorsettings.db.DbTransactor

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    DbTransactor.transactor().use { transactor =>
      for {
        _ <- DbTransactor.migrate(transactor)
        exitCode <- AppServer
          .stream(transactor)
          .compile
          .drain
          .as(ExitCode.Success)
      } yield exitCode
    }
  }
}