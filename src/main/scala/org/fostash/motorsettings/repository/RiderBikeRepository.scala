package org.fostash.motorsettings.repository

import fs2.Stream
import org.fostash.motorsettings.db.dao.EntityNotFoundError
import org.fostash.motorsettings.repository.model.{RiderBikeRequest, RiderBikeResponse}

trait RiderBikeRepository[F[_]] {

  def addBikeToRider(rider: RiderBikeRequest): F[RiderBikeResponse]
  def readRiderBikes(riderId: Long): Stream[F, RiderBikeResponse]
  def updateRiderBike(bikeRiderId: Long, rider: RiderBikeRequest): F[Either[EntityNotFoundError.type, Boolean]]
  def deleteBikeFromRider(riderId: Long): F[Either[EntityNotFoundError.type, Boolean]]
}