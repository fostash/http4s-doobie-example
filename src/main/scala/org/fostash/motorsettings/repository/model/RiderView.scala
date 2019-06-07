package org.fostash.motorsettings.repository.model
import java.time.LocalDate

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class RiderRequest(firstName: String, lastName: String, nickname: String, birthday: String)
case class RiderResponse(id: Long, firstName: String, lastName: String, nickname: String, birthday: String)

case class RiderBikeRequest(riderId: Long, bikeId: Long, name: String, maxSpeed: BigDecimal, cc: Double)
case class RiderBikeResponse(id: Long, riderId: Long, bikeId: Long, name: String, maxSpeed: BigDecimal, cc: Double)

case class RiderTimeTableRequest(circuitId: Long, riderId: Long, bikeId: Long, sessionDate: String)
case class RiderTimeTableResponse(id: Long, circuitId: Long, riderId: Long, bikeId: Long, sessionDate: LocalDate)

case class RiderLapRequest(timeTableId: Long, lapNum: Int, minutes: Int, seconds: Int, millis: Int)
case class RiderLapResponse(id: Long, lapNum: Int, minutes: Int, seconds: Int, millis: Int)

object RiderSerializer {

  implicit val riderRequestDecoder: Decoder[RiderRequest] = deriveDecoder[RiderRequest]
  implicit val riderResponseDecoder: Decoder[RiderResponse] = deriveDecoder[RiderResponse]
  implicit val riderBikeRequestDecoder: Decoder[RiderBikeRequest] = deriveDecoder[RiderBikeRequest]
  implicit val riderBikeResponseDecoder: Decoder[RiderBikeResponse] = deriveDecoder[RiderBikeResponse]
  implicit val riderTimeTableRequestDecoder: Decoder[RiderTimeTableRequest] = deriveDecoder[RiderTimeTableRequest]

  implicit val riderTimeTableResponseEncoder: Encoder[RiderTimeTableResponse] = deriveEncoder[RiderTimeTableResponse]
  implicit val riderLapRequestEncoder: Encoder[RiderLapRequest] = deriveEncoder[RiderLapRequest]
  implicit val riderLapResponseEncoder: Encoder[RiderLapResponse] = deriveEncoder[RiderLapResponse]
}