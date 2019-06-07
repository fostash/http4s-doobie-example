package org.fostash.motorsettings.repository.model

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}


case class BikeRequest(brand: String, cc: Int)
case class BikeResponse(id: Long, brand: String, cc: Int)

object BikeSerializer {

  implicit val bikeRequestDecoder: Decoder[BikeRequest] = deriveDecoder[BikeRequest]
  implicit val bikeResponseEncoder: Encoder[BikeResponse] = deriveEncoder[BikeResponse]
}

