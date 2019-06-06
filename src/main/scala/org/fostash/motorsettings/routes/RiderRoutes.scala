package org.fostash.motorsettings.routes

import cats.effect.IO
import fs2.Stream
import io.chrisdavenport.log4cats.Logger
import io.circe.generic.auto._
import io.circe.syntax._
import org.fostash.motorsettings.db.dao.EntityNotFoundError
import org.fostash.motorsettings.repository.{RiderBikeRepository, RiderRepository}
import org.fostash.motorsettings.repository.model.{RiderBikeRequest, RiderRequest}
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class RiderRoutes(riderRepository: RiderRepository[IO], riderBikeRepository: RiderBikeRepository[IO])(implicit L: Logger[IO]) extends Http4sDsl[IO] {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ POST -> Root / "riders" => for {
      _ <- L.info(s"REQUEST $req")
      body <- req.decodeJson[RiderRequest]
      rider <- riderRepository.insertRider(body)
      response <- Created(rider.asJson)
      _ <- L.info(s"RESPONSE $response")
    } yield response

    case req @ POST -> Root / "riders" / LongVar(riderId) => for {
      _ <- L.info(s"REQUEST $req")
      body <- req.decodeJson[RiderRequest]
      rider <- riderRepository.updateRider(riderId, body)
      response <- Created(rider.asJson)
      _ <- L.info(s"RESPONSE $response")
    } yield response

    case GET -> Root / "riders" / LongVar(riderId) => for {
      _ <- L.info(s"REQUEST $riderId")
      result <- riderRepository.readRider(riderId)
      response <- result match {
        case Left(EntityNotFoundError) => NotFound()
        case Right(rider)             => Ok(rider.asJson)
      }
      _ <- L.info(s"RESPONSE $response")
    } yield response

    case GET -> Root / "riders" :? FilterQueryParamMatcher(filter) => for {
      _ <- L.info(s"REQUEST search riders: filter $filter")
      response <- Ok(Stream("[") ++ riderRepository.readRiders(filter).map(_.asJson.noSpaces).intersperse(",") ++ Stream("]"))
      _ <- L.info(s"RESPONSE $response")
    } yield response

    case req @ POST -> Root / "riders" / "bikes" => for {
      _ <- L.info(s"REQUEST add bike to rider $req")
      request <- req.decodeJson[RiderBikeRequest]
      riderBike <- riderBikeRepository.addBikeToRider(request)
      response <- Created(riderBike.asJson)
      _ <- L.info(s"RESPONSE $response")
    } yield response

    case GET -> Root / "riders" / LongVar(riderId) / "bikes" => for {
      _ <- L.info(s"REQUEST read rider bikes")
      response <- Ok(Stream("[") ++ riderBikeRepository.readRiderBikes(riderId).map(_.asJson.noSpaces).intersperse(",") ++ Stream("]"))
      _ <- L.info(s"RESPONSE $response")
    } yield response
  }
}