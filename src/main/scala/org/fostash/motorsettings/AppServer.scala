package org.fostash.motorsettings

import cats.effect._
import doobie.util.transactor.Transactor
import fs2.Stream
import io.chrisdavenport.log4cats.SelfAwareStructuredLogger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import org.fostash.motorsettings.routes.Routes
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

object AppServer {

  def stream[F[_]: ConcurrentEffect](transactor: Transactor[F])(implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {
    implicit val log: SelfAwareStructuredLogger[F] = Slf4jLogger.unsafeCreate[F]
    BlazeServerBuilder[F]
        .bindHttp(8080, "localhost")
        .withHttpApp(Logger.httpApp(logHeaders = true, logBody = true)(Routes.routes(transactor)))
        .serve.drain
  }
}