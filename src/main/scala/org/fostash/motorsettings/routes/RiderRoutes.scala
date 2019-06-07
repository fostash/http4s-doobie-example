package org.fostash.motorsettings.routes

import cats.{Monad, MonadError}
import cats.effect._
import cats.implicits._
import fs2.Stream
import io.chrisdavenport.log4cats.Logger
import io.circe.generic.auto._
import io.circe.syntax._
import org.fostash.motorsettings.db.dao.EntityNotFoundError
import org.fostash.motorsettings.repository.model.{RiderBikeRequest, RiderRequest}
import org.fostash.motorsettings.repository.{RiderBikeRepository, RiderRepository}
import org.http4s.circe._
import org.http4s.{HttpRoutes, Request}
import org.http4s.dsl.Http4sDsl

class RiderRoutes[F[_]: Monad: Sync](riderRepository: RiderRepository[F], riderBikeRepository: RiderBikeRepository[F])(implicit L: Logger[F], F: MonadError[F, Throwable])
  extends Http4sDsl[F] {

  import org.fostash.motorsettings.repository.model.RiderSerializer._

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "riders" =>
      saveRider(req)

    case req @ POST -> Root / "riders" / LongVar(riderId) =>
      updateRider(riderId, req)

    case GET -> Root / "riders" / LongVar(riderId) =>
      riderById(riderId)

    case GET -> Root / "riders" :? FilterQueryParamMatcher(filter) =>
      searchRiders(filter)

    case req @ POST -> Root / "riders" / "bikes" =>
      addBikeToRider(req)

    case GET -> Root / "riders" / LongVar(riderId) / "bikes" =>
      riderBikes(riderId)
  }

  private def saveRider(request: Request[F]) =
    for {
      _ <- L.info(s"REQUEST $request")
      body <- request.decodeJson[RiderRequest]
      rider <- riderRepository.insertRider(body)
      response <- Created(rider.asJson)
      _ <- L.info(s"RESPONSE $response")
    } yield response

  private def updateRider(riderId: Long, request: Request[F]) =
    for {
      _ <- L.info(s"REQUEST $request")
      body <- request.decodeJson[RiderRequest]
      rider <- riderRepository.updateRider(riderId, body)
      response <- Created(rider.asJson)
      _ <- L.info(s"RESPONSE $response")
    } yield response

  private def riderById(riderId: Long) =
    for {
      _ <- L.info(s"REQUEST $riderId")
      result <- riderRepository.readRider(riderId)
      response <- result match {
        case Left(EntityNotFoundError) => NotFound()
        case Right(rider)              => Ok(rider.asJson)
      }
      _ <- L.info(s"RESPONSE $response")
    } yield response

  private def searchRiders(filter: String) =
    for {
      _ <- L.info(s"REQUEST search riders: filter $filter")
      response <- Ok(
        Stream("[") ++ riderRepository
          .readRiders(filter)
          .map(_.asJson.noSpaces)
          .intersperse(",") ++ Stream("]")
      )
      _ <- L.info(s"RESPONSE $response")
    } yield response

  private def addBikeToRider(request: Request[F]) =
    for {
      _ <- L.info(s"REQUEST add bike to rider $request")
      request <- request.decodeJson[RiderBikeRequest]
      riderBike <- riderBikeRepository.addBikeToRider(request)
      response <- Created(riderBike.asJson)
      _ <- L.info(s"RESPONSE $response")
    } yield response

  private def riderBikes(riderId: Long) =
    for {
      _ <- L.info(s"REQUEST read rider bikes")
      response <- Ok(
        Stream("[") ++ riderBikeRepository
          .readRiderBikes(riderId)
          .map(_.asJson.noSpaces)
          .intersperse(",") ++ Stream("]")
      )
      _ <- L.info(s"RESPONSE $response")
    } yield response
}