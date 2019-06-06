package org.fostash.motorsettings.repository

import fs2.Stream
import org.fostash.motorsettings.db.dao.EntityNotFoundError
import org.fostash.motorsettings.repository.model.{BikeRequest, BikeResponse}

trait BikeRepository[F[_]] {

  def insertBike(bike: BikeRequest): F[BikeResponse]
  def readBikes(filter: String): Stream[F, BikeResponse]
  def readBike(bikeId: Long): F[Either[EntityNotFoundError.type, BikeResponse]]
  def updateBike(bikeId: Long, bike: BikeRequest): F[Either[EntityNotFoundError.type, Boolean]]
  def deleteBike(bikeId: Long): F[Either[EntityNotFoundError.type, Boolean]]
}