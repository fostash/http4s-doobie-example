package org.fostash.motorsettings.repository

import fs2.Stream
import org.fostash.motorsettings.db.dao.EntityNotFoundError
import org.fostash.motorsettings.repository.model.{RiderRequest, RiderResponse}

trait RiderRepository[F[_]] {

  def insertRider(rider: RiderRequest): F[RiderResponse]
  def readRiders(filter: String): Stream[F, RiderResponse]
  def readRider(riderId: Long): F[Either[EntityNotFoundError.type, RiderResponse]]
  def updateRider(riderId: Long, rider: RiderRequest): F[Either[EntityNotFoundError.type, Boolean]]
  def deleteRider(riderId: Long): F[Either[EntityNotFoundError.type, Boolean]]
}