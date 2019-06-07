package org.fostash.motorsettings.db.dao

import cats.effect._
import cats.implicits._
import doobie.implicits._
import doobie.util.transactor.Transactor
import fs2.Stream
import io.chrisdavenport.log4cats.Logger
import org.fostash.motorsettings.db.converter.BikeConverter
import org.fostash.motorsettings.db.entities.Bike
import org.fostash.motorsettings.repository.BikeRepository
import org.fostash.motorsettings.repository.model.{BikeRequest, BikeResponse}

object BikeQueries extends BaseQueryOps {

  def readBike(bikeId: Long): doobie.Query0[Bike] = sql"SELECT id, brand, cc FROM bikes WHERE id = $bikeId".query[Bike]
  def readBikes(filter: String): doobie.Query0[Bike] = sql"SELECT id, brand, cc FROM bikes WHERE brand like ${likeStr(filter)}".query[Bike]
  def insertBike(bike: Bike): doobie.Update0 = sql"INSERT INTO bikes (brand, cc) VALUES (${bike.brand}, ${bike.cc})".update
  def updateBike(bikeId: Long, brand: String, cc: Int): doobie.Update0 = sql"UPDATE bikes SET brand = '$brand', cc = $cc WHERE id = $bikeId".update
  def deleteBike(bikeId: Long): doobie.Update0 = sql"DELETE FROM bikes WHERE id = $bikeId".update
}

class BikeDao[F[_]](val transactor: Transactor[F])(implicit l: Logger[F], w: Bracket[F, Throwable], F: Sync[F]) extends BikeRepository[F] with BaseOps[F] {

  override def insertBike(bikeRequest: BikeRequest): F[BikeResponse] = for {
    bike <- F.pure(BikeConverter.from(bikeRequest))
    response <- insert { BikeQueries.insertBike(bike) }.map { id => bike.copy(id = id) }
  } yield BikeConverter.from(response)

  override def readBikes(filter: String): Stream[F, BikeResponse] = {
    l.info(BikeQueries.readBikes(filter).sql)
    BikeQueries.readBikes(filter).stream.transact(transactor).map(BikeConverter.from)
  }

  override def readBike(bikeId: Long): F[Either[EntityNotFoundError.type, BikeResponse]] = {
    l.info(BikeQueries.readBike(bikeId).sql)
    read(BikeQueries.readBike(bikeId), BikeConverter.from)
  }

  override def updateBike(bikeId: Long, bikeRequest: BikeRequest): F[Either[EntityNotFoundError.type, Boolean]] = for {
      bike <- F.pure(BikeConverter.from(bikeRequest))
      response <- updelete(BikeQueries.updateBike(bikeId, bike.brand, bike.cc))
    } yield response

  override def deleteBike(bikeId: Long): F[Either[EntityNotFoundError.type, Boolean]] =
    updelete(BikeQueries.deleteBike(bikeId))
}