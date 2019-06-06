package org.fostash.motorsettings.routes

import cats.effect._
import fs2.Stream
import io.circe._
import org.fostash.motorsettings.Util
import org.fostash.motorsettings.repository.{RiderBikeRepository, RiderRepository}
import org.fostash.motorsettings.repository.model.RiderResponse
import org.http4s.circe._
import org.http4s.implicits._
import org.http4s.{HttpRoutes, Method, Request, Status, Uri}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

class RiderRoutesSpec extends WordSpec with Matchers with MockFactory with Util  {

  val mockRiderRepository: RiderRepository[IO] = stub[RiderRepository[IO]]
  val mockRiderBikeRepository: RiderBikeRepository[IO] = stub[RiderBikeRepository[IO]]
  val routes: HttpRoutes[IO] = new RiderRoutes(mockRiderRepository, mockRiderBikeRepository).routes

  "RiderRoutes" should {

    /*"insert rider" in {

      val expectedJson = Json.obj(
        ("id", Json.fromLong(1L)),
        ("firstName",  Json.fromString("fistname")),
        ("lastName",  Json.fromString("lastname")),
        ("nickname",  Json.fromString("nickname")),
        ("birthday",  Json.fromString("birthday"))
      )

      val mockRiderRequest = RiderRequest("firstname", "lastname", "nickname", "birthday")
      val mockRiderResponse = model.RiderResponse(1L, "firstname", "lastname", "nickname", "birthday")
      (mockRepository.insertRider _).when(mockRiderRequest)
        .returns(IO.pure(mockRiderResponse))

      val mockRequest = Request[IO](Method.POST, Uri.unsafeFromString("/riders"), "")
      val response = routes.orNotFound.run(mockRequest)
      check[Json](response, Status.Ok, Some(expectedJson)) shouldBe true
    }*/

    "read rider" in {

      (mockRiderRepository.readRider _).when(1L)
        .returns(IO.pure(Right(RiderResponse(1L, "", "", "", ""))))

      val mockRequest = Request[IO](Method.GET, Uri.unsafeFromString("/riders/1"))
      val response = routes.orNotFound.run(mockRequest)

      val expectedJson = Json.obj(
        ("id", Json.fromLong(1L)),
        ("firstName",  Json.fromString("")),
        ("lastName",  Json.fromString("")),
        ("nickname",  Json.fromString("")),
        ("birthday",  Json.fromString(""))
      )
      check[Json](response, Status.Ok, Some(expectedJson)) shouldBe true
    }

    "read riders" in {

      (mockRiderRepository.readRiders _).when("test")
        .returns(Stream(RiderResponse(1L, "", "", "", ""), RiderResponse(2L, "", "", "", "")))

      val mockRequest = Request[IO](Method.GET, Uri.unsafeFromString("/riders?filter=test"))
      val response = routes.orNotFound.run(mockRequest)

      val expectedJson = Json.arr(
        Json.obj(
          ("id", Json.fromLong(1L)),
          ("firstName",  Json.fromString("")),
          ("lastName",  Json.fromString("")),
          ("nickname",  Json.fromString("")),
          ("birthday",  Json.fromString(""))
        ),
        Json.obj(
          ("id", Json.fromLong(2L)),
          ("firstName",  Json.fromString("")),
          ("lastName",  Json.fromString("")),
          ("nickname",  Json.fromString("")),
          ("birthday",  Json.fromString(""))
        )
      )
      check[Json](response, Status.Ok, Some(expectedJson)) shouldBe true
    }
  }

}
