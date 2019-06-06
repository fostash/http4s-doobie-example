package org.fostash.motorsettings.repository.model
import java.time.LocalDate

case class RiderRequest(firstName: String, lastName: String, nickname: String, birthday: String)
case class RiderResponse(id: Long, firstName: String, lastName: String, nickname: String, birthday: String)

case class RiderBikeRequest(riderId: Long, bikeId: Long, name: String, maxSpeed: BigDecimal, cc: Double)
case class RiderBikeResponse(id: Long, riderId: Long, bikeId: Long, name: String, maxSpeed: BigDecimal, cc: Double)

case class RiderTimeTableRequest(circuitId: Long, riderId: Long, bikeId: Long, sessionDate: String)
case class RiderTimeTableResponse(id: Long, circuitId: Long, riderId: Long, bikeId: Long, sessionDate: LocalDate)

case class RiderLapRequest(timeTableId: Long, lapNum: Int, minutes: Int, seconds: Int, millis: Int)
case class RiderLapResponse(id: Long, lapNum: Int, minutes: Int, seconds: Int, millis: Int)