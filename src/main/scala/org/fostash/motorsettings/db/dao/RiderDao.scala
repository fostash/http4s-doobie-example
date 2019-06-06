package org.fostash.motorsettings.db.dao

import fs2.Stream
import cats.effect._
import cats.implicits._
import doobie.implicits._
import doobie.util.transactor.Transactor
import io.chrisdavenport.log4cats.Logger
import org.fostash.motorsettings.db.converter.RiderConverter
import org.fostash.motorsettings.db.entities.Rider
import org.fostash.motorsettings.repository.RiderRepository
import org.fostash.motorsettings.repository.model.{RiderRequest, RiderResponse}

object RiderQueries {

  def readRider(riderId: Long): doobie.Query0[Rider] =
    sql"SELECT id, first_name, last_name, nickname, birthday FROM riders WHERE id = $riderId".query[Rider]

  def readRiders(filter: String): doobie.Query0[Rider] =
    sql"""SELECT id, first_name, last_name, nickname, birthday FROM riders WHERE name like '%$filter%'""".query[Rider]

  def insertRider(rider: Rider): doobie.Update0 =
    sql"""INSERT INTO riders (first_name, last_name, nickname, birthday)
         |VALUES ('${rider.firstName}', '${rider.lastName}', '${rider.nickname}', ${rider.birthday})""".update

  def updateRider(rider: Rider): doobie.Update0 =
    sql"""UPDATE riders
         |SET first_name = '${rider.firstName}',
         |  last_name = '${rider.lastName}',
         |  nickname = '${rider.nickname}',
         |  birthday = ${rider.birthday}
         |WHERE id = ${rider.id}""".stripMargin.update

  def deleteRider(riderId: Long): doobie.Update0 =
    sql"DELETE FROM riders WHERE id = $riderId".update
}

class RiderDao[F[_]](val transactor: Transactor[F])(implicit l: Logger[F], w: Bracket[F, Throwable], F: Sync[F]) extends RiderRepository[F] with BaseOps[F] {

  override def insertRider(riderRequest: RiderRequest): F[RiderResponse] = for {
    rider <- F.pure(RiderConverter.from(riderRequest))
    response <- insert(RiderQueries.insertRider(rider)).map { id => rider.copy(id = id) }
  } yield RiderConverter.from(response)

  override def readRiders(filter: String): Stream[F, RiderResponse] =
    stream(RiderQueries.readRiders(filter), RiderConverter.from)

  override def readRider(riderId: Long): F[Either[EntityNotFoundError.type, RiderResponse]] =
    read(RiderQueries.readRider(riderId), RiderConverter.from)

  override def updateRider(riderId: Long, riderRequest: RiderRequest): F[Either[EntityNotFoundError.type, Boolean]] = for {
    rider <- F.pure(RiderConverter.from(riderRequest))
    response <- updelete(RiderQueries.updateRider(rider))
  } yield response

  override def deleteRider(riderId: Long): F[Either[EntityNotFoundError.type, Boolean]] =
    updelete(RiderQueries.deleteRider(riderId))
}