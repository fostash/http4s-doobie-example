package org.fostash.motorsettings.routes

import cats.effect.IO
import fs2.Stream
import io.chrisdavenport.log4cats.Logger
import io.circe.generic.auto._
import io.circe.syntax._
import org.fostash.motorsettings.repository.RiderTimeTableRepository
import org.fostash.motorsettings.repository.model.RiderTimeTableRequest
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class RiderTimeTableRoutes(riderTimeTableRepository: RiderTimeTableRepository[IO])(implicit L: Logger[IO]) extends Http4sDsl[IO] {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {

    case req @ POST -> Root / "riders" / "timetable" => for {
      _ <- L.info(s"REQUEST $req")
      body <- req.decodeJson[RiderTimeTableRequest]
      timeTable <- riderTimeTableRepository.createTimeTable(body)
      response <- Created(timeTable.asJson)
      _ <- L.info(s"RESPONSE $response")
    } yield response

    case GET -> Root / "riders" / LongVar(riderId) / "timetables" => for {
      _ <- L.info(s"REQUEST $riderId")
      response <- Ok(Stream("[") ++ riderTimeTableRepository.readRiderTimeTables(riderId).map(_.asJson.noSpaces).intersperse(",") ++ Stream("]"))
      _ <- L.info(s"RESPONSE $response")
    } yield response
  }
}