package org.fostash.motorsettings.repository.model

import cats.Applicative
import cats.effect.Sync
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder, Json}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}


case class BikeRequest(brand: String, cc: Int)
case class BikeResponse(id: Long, brand: String, cc: Int)

object BikeRequest {

  implicit val bikeRequestEncoder: Encoder[BikeRequest] = deriveEncoder[BikeRequest]
  implicit def bikeEntityEncoder[F[_]: Applicative]: EntityEncoder[F, BikeRequest] =
    jsonEncoderOf[F, BikeRequest]

  implicit val bikeDecoder: Decoder[BikeRequest] = deriveDecoder[BikeRequest]
  implicit def bikeEntityDecoder[F[_]: Sync]: EntityDecoder[F, BikeRequest] = jsonOf[F, BikeRequest]
}

object BikeResponse {

  implicit val bikeResponseEncoder: Encoder[BikeResponse] = new Encoder[BikeResponse] {
    final def apply(bike: BikeResponse): Json = Json.obj(
      ("id", Json.fromLong(bike.id)),
      ("cc", Json.fromInt(bike.cc)),
      ("brand", Json.fromString(bike.brand)),
    )
  }

  implicit def bikeResponseEntityEncoder[F[_]: Applicative]: EntityEncoder[F, BikeResponse] =
    jsonEncoderOf[F, BikeResponse]
}

