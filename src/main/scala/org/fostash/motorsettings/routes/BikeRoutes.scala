package org.fostash.motorsettings.routes

import cats.effect._
import cats.implicits._
import cats.{Monad, MonadError}
import fs2.Stream
import io.chrisdavenport.log4cats.Logger
import io.circe.generic.auto._
import io.circe.syntax._
import org.fostash.motorsettings.db.dao.EntityNotFoundError
import org.fostash.motorsettings.repository.BikeRepository
import org.fostash.motorsettings.repository.model.BikeRequest
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Request}
import org.fostash.motorsettings.repository.model.BikeSerializer._

class BikeRoutes[F[_]: Monad: Sync](repository: BikeRepository[F])(implicit L: Logger[F], F: MonadError[F, Throwable]) extends Http4sDsl[F] {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "bikes" => save(req)

    case req @ PUT -> Root / "bikes" / LongVar(bikeId) =>
      bikeById(req, bikeId)

    case GET -> Root / "bikes" / LongVar(bikeId) => readBike(bikeId)

    case GET -> Root / "bikes" :? FilterQueryParamMatcher(filter) =>
      Ok(
        Stream("[") ++ repository
          .readBikes(filter)
          .map(_.asJson.noSpaces)
          .intersperse(",") ++ Stream("]")
      )
  }

  private def save(req: Request[F]) =
    for {
      _ <- L.info(s"REQUEST $req")
      body <- req.decodeJson[BikeRequest]
      bike <- repository.insertBike(body)
      response <- Created(bike.asJson)
      _ <- L.info(s"RESPONSE $response")
    } yield response

  private def bikeById(req: Request[F], bikeId: Long) =
    for {
      _ <- L.info(s"REQUEST $req")
      body <- req.decodeJson[BikeRequest]
      bike <- repository.updateBike(bikeId, body)
      response <- Created(bike.asJson)
      _ <- L.info(s"RESPONSE $response")
    } yield response

  private def readBike(bikeId: Long) =
    for {
      _ <- L.info(s"REQUEST $bikeId")
      result <- repository.readBike(bikeId)
      response <- result match {
        case Left(EntityNotFoundError) => NotFound()
        case Right(bike)               => Ok(bike.asJson)
      }
      _ <- L.info(s"RESPONSE $response")
    } yield response

}