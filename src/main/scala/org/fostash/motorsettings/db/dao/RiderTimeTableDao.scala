package org.fostash.motorsettings.db.dao
import cats.effect.{Bracket, IO, Sync}
import cats.implicits._
import doobie.implicits._
import doobie.util.transactor.Transactor
import io.chrisdavenport.log4cats.Logger
import org.fostash.motorsettings.db.converter.TimeTableConverter
import org.fostash.motorsettings.db.entities.{RiderLap, RiderTimeTable}
import org.fostash.motorsettings.repository.RiderTimeTableRepository
import org.fostash.motorsettings.repository.model.{RiderLapRequest, RiderLapResponse, RiderTimeTableRequest, RiderTimeTableResponse}

object RiderTimeTableQueries {

  def insertTimeTable(riderTimeTable: RiderTimeTable): doobie.Update0 =
    sql"""INSERT INTO time_table (circuit_id, rider_id, bike_id, session_date)
         |VALUES (${riderTimeTable.circuitId}, ${riderTimeTable.riderId}, ${riderTimeTable.bikeId}, '${riderTimeTable.sessionDate}')""".update

  def readRiderTimeTables(riderId: Long): doobie.Query0[RiderTimeTable] =
    sql"SELECT id, session_date, circuit_id, rider_id, bike_id FROM time_table WHERE rider_id = $riderId"
      .query[RiderTimeTable]

  def insertLap(riderLap: RiderLap): doobie.Update0 =
    sql"""INSERT INTO laps (time_table_id, lap_number, minutes, seconds, millis)
         |VALUES (${riderLap.timeTableId}, ${riderLap.lapNum}, ${riderLap.minutes}, ${riderLap.seconds}, ${riderLap.millis})""".update
}

class RiderTimeTableDao[F[_]](val transactor: Transactor[F])(implicit l: Logger[F], w: Bracket[F, Throwable], F: Sync[F]) extends RiderTimeTableRepository[F] with BaseOps[F] {

  override def createTimeTable(riderTimeTableRequest: RiderTimeTableRequest): F[RiderTimeTableResponse] = for {
    riderTimeTable <- F.pure(TimeTableConverter.from(riderTimeTableRequest))
    timetable <- insert(RiderTimeTableQueries.insertTimeTable(riderTimeTable)).map { id => riderTimeTable.copy(id = id) }
  } yield TimeTableConverter.from(timetable)

  override def readRiderTimeTables(riderId: Long): fs2.Stream[F, RiderTimeTableResponse] =
    stream(RiderTimeTableQueries.readRiderTimeTables(riderId), TimeTableConverter.from)

  override def readTimeTableLaps(timeTableId: Long): fs2.Stream[F, RiderLapResponse] =
    stream(sql"SELECT id, lap_number, minutes, seconds, millis FROM laps WHERE time_table_id = $timeTableId"
      .query[RiderLap], TimeTableConverter.from)

  override def addLapToTimeTable(riderLapRequest: RiderLapRequest): F[RiderLapResponse] = for {
    riderLap <- F.pure(TimeTableConverter.from(riderLapRequest))
    lap <- insert(RiderTimeTableQueries.insertLap(riderLap)).map { id => riderLap.copy(id = id) }
  } yield TimeTableConverter.from(lap)
}
