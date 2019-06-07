package org.fostash.motorsettings.db

import cats.effect._
import doobie.hikari._
import doobie.util.ExecutionContexts
import org.flywaydb.core.Flyway
import org.fostash.motorsettings.Config

object DbTransactor { //extends Database[IO] {

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContexts.synchronous)

  def transactor(): Resource[IO, HikariTransactor[IO]] = for {
    ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
    te <- ExecutionContexts.cachedThreadPool[IO]    // our transaction EC
    xa <- HikariTransactor.newHikariTransactor[IO](
      Config.DatabaseDriver,
      Config.DatabaseUrl,
      Config.DatabaseUsername,
      Config.DatabasePassword,                // password
      ce,                                     // await connection here
      te                                      // execute JDBC operations here
    )
  } yield xa

  def migrate(transactor: HikariTransactor[IO]): IO[Unit] = {
    transactor.configure { dataSource =>
      IO {
        val flyWay = Flyway.configure().dataSource(dataSource).load()
        flyWay.repair()
        flyWay.migrate()
        ()
      }
    }
  }
}
