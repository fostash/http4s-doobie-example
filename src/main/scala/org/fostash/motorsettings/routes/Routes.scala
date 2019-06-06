package org.fostash.motorsettings.routes

import cats.effect._
import doobie.util.transactor.Transactor
import io.chrisdavenport.log4cats.Logger
import org.fostash.motorsettings.db.dao.BikeDao
import org.http4s.dsl.impl.QueryParamDecoderMatcher
import org.http4s.implicits._
import org.http4s.server.Router

object FilterQueryParamMatcher extends QueryParamDecoderMatcher[String]("filter")

object Routes {

  def routes[F[_]](transactor: Transactor[F])(implicit l: Logger[F], w: Bracket[F, Throwable], F: Sync[F]) =
    Router("/api" -> new BikeRoutes(new BikeDao(transactor)).routes).orNotFound

}