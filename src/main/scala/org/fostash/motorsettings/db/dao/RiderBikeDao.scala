package org.fostash.motorsettings.db.dao

import cats.effect._
import cats.implicits._
import doobie.implicits._
import doobie.util.transactor.Transactor
import fs2.Stream
import io.chrisdavenport.log4cats.Logger
import org.fostash.motorsettings.db.converter.RiderConverter
import org.fostash.motorsettings.db.entities.RiderBike
import org.fostash.motorsettings.repository.RiderBikeRepository
import org.fostash.motorsettings.repository.model.{RiderBikeRequest, RiderBikeResponse}

object RiderBikeQueries {

  def insertRiderBike(riderBike: RiderBike): doobie.Update0 =
    sql"""INSERT INTO rider_bikes (rider_id, bike_id, name, max_speed, cc)
         |VALUES (${riderBike.riderId}, ${riderBike.bikeId}, '${riderBike.name}', ${riderBike.maxSpeed}, ${riderBike.cc})""".update

  def readRiderBikes(riderBikeId: Long): doobie.Query0[RiderBike] =
    sql"""SELECT id, rider_id, bike_id, name, max_speed, cc FROM rider_bikes WHERE id = $riderBikeId""".query[RiderBike]

  def updateRiderBike(riderBike: RiderBike): doobie.Update0 =
    sql"""UPDATE rider_bikes
         |SET rider_id = ${riderBike.riderId},
         |  bike_id = ${riderBike.bikeId},
         |  name = ${riderBike.name},
         |  max_speed = ${riderBike.maxSpeed}
         |  cc = ${riderBike.cc}
         |WHERE id = ${riderBike.id}""".stripMargin.update

  def deleteRiderBike(riderBikeId: Long): doobie.Update0 =
    sql"DELETE FROM rider_bikes WHERE id = $riderBikeId".update
}

class RiderBikeDao[F[_]](val transactor: Transactor[F])(implicit l: Logger[F], w: Bracket[F, Throwable], F: Sync[F]) extends RiderBikeRepository[F] with BaseOps[F] {

  override def addBikeToRider(
    riderBikeRequest: RiderBikeRequest): F[RiderBikeResponse] = for {
    riderBike <- F.pure(RiderConverter.from(riderBikeRequest))
    response <- insert(RiderBikeQueries.insertRiderBike(riderBike)).map { id => riderBike.copy(id = id) }
  } yield RiderConverter.from(response)

  override def readRiderBikes(riderBikeId: Long): Stream[F, RiderBikeResponse] =
    stream(RiderBikeQueries.readRiderBikes(riderBikeId), RiderConverter.from)

  override def updateRiderBike(riderId: Long, riderBikeRequest: RiderBikeRequest): F[Either[EntityNotFoundError.type, Boolean]] = for {
    riderBike <- F.pure(RiderConverter.from(riderBikeRequest))
    response <- updelete(RiderBikeQueries.updateRiderBike(riderBike))
  } yield response

  override def deleteBikeFromRider(riderBikeId: Long): F[Either[EntityNotFoundError.type, Boolean]] =
    updelete(RiderBikeQueries.deleteRiderBike(riderBikeId))
}