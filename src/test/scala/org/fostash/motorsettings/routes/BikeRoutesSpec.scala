package org.fostash.motorsettings.routes

import cats.effect._
import fs2.Stream
import io.circe._
import org.fostash.motorsettings.Util
import org.fostash.motorsettings.repository.BikeRepository
import org.fostash.motorsettings.repository.model.BikeResponse
import org.http4s.circe._
import org.http4s.implicits._
import org.http4s.{HttpRoutes, Method, Request, Status, Uri}
import org.scalamock.scalatest._
import org.scalatest.{Matchers, WordSpec}

class BikeRoutesSpec extends WordSpec with Matchers with MockFactory with Util {

  val mockRepository: BikeRepository[IO] = stub[BikeRepository[IO]]
  val routes: HttpRoutes[IO] = new BikeRoutes(mockRepository).routes

  "BikeRoutes" should {

    "read bike" in {

      (mockRepository.readBike _).when(1L)
        .returns(IO.pure(Right(BikeResponse(1L, "", 1))))

      val mockRequest = Request[IO](Method.GET, Uri.unsafeFromString("/bikes/1"))
      val response = routes.orNotFound(mockRequest)

      val expectedJson = Json.obj(
        ("id", Json.fromLong(1L)),
        ("brand",  Json.fromString("")),
        ("cc",  Json.fromInt(1))
      )
      check[Json](response, Status.Ok, Some(expectedJson)) shouldBe true
    }

    "read bikes" in {

      (mockRepository.readBikes _).when("test")
        .returns(Stream(BikeResponse(1L, "", 1), BikeResponse(2L, "", 1)))

      val mockRequest = Request[IO](Method.GET, Uri.unsafeFromString("/bikes?filter=test"))
      val response = routes.orNotFound.run(mockRequest)

      val expectedJson = Json.arr(
        Json.obj(
          ("id", Json.fromLong(1L)),
          ("brand",  Json.fromString("")),
          ("cc",  Json.fromInt(1))
      ),
        Json.obj(
          ("id", Json.fromLong(2L)),
          ("brand",  Json.fromString("")),
          ("cc",  Json.fromInt(1))
        )
      )
      check[Json](response, Status.Ok, Some(expectedJson)) shouldBe true
    }
  }

}
