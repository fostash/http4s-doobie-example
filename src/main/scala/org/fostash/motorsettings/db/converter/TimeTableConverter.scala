package org.fostash.motorsettings.db.converter
import java.time.LocalDate

import org.fostash.motorsettings.db.entities.{RiderLap, RiderTimeTable}
import org.fostash.motorsettings.repository.model.{RiderLapRequest, RiderLapResponse, RiderTimeTableRequest, RiderTimeTableResponse}

object TimeTableConverter {

  def from(timeTableRequest: RiderTimeTableRequest): RiderTimeTable =
    RiderTimeTable(0L,
      circuitId = timeTableRequest.circuitId,
      riderId = timeTableRequest.riderId,
      bikeId = timeTableRequest.bikeId,
      sessionDate = timeTableRequest.sessionDate
    )

  def from(riderTimeTable: RiderTimeTable): RiderTimeTableResponse =
    RiderTimeTableResponse(
      id = riderTimeTable.id,
      circuitId = riderTimeTable.circuitId,
      riderId = riderTimeTable.riderId,
      bikeId = riderTimeTable.bikeId,
      sessionDate = LocalDate.parse(riderTimeTable.sessionDate)
    )

  def from(riderLapsRequest: RiderLapRequest): RiderLap =
    RiderLap(0L,
      timeTableId = riderLapsRequest.timeTableId,
      lapNum = riderLapsRequest.lapNum,
      minutes = riderLapsRequest.minutes,
      seconds = riderLapsRequest.seconds,
      millis = riderLapsRequest.millis
    )

  def from(riderLap: RiderLap): RiderLapResponse =
    RiderLapResponse(
      id = riderLap.id,
        lapNum = riderLap.lapNum,
        minutes = riderLap.minutes,
        seconds = riderLap.seconds,
        millis = riderLap.millis
    )
}
