package org.fostash.motorsettings.repository

import fs2.Stream
import org.fostash.motorsettings.repository.model._

trait RiderTimeTableRepository[F[_]] {

  def createTimeTable(body: RiderTimeTableRequest): F[RiderTimeTableResponse]
  def readRiderTimeTables(riderId: Long): Stream[F, RiderTimeTableResponse]
  def readTimeTableLaps(timeTableId: Long): Stream[F, RiderLapResponse]
  def addLapToTimeTable(riderLapsRequest: RiderLapRequest): F[RiderLapResponse]

}