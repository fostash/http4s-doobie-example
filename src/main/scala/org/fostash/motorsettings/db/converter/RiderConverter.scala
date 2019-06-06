package org.fostash.motorsettings.db.converter

import org.fostash.motorsettings.db.entities.{Rider, RiderBike}
import org.fostash.motorsettings.repository.model.{RiderBikeRequest, RiderBikeResponse, RiderRequest, RiderResponse}

object RiderConverter {

  def from(riderRequest: RiderRequest): Rider =
    Rider(0L,
      riderRequest.firstName,
      riderRequest.lastName,
      riderRequest.nickname,
      riderRequest.birthday)

  def from(rider: Rider): RiderResponse =
    RiderResponse(
      rider.id,
      rider.firstName,
      rider.lastName,
      rider.nickname,
      rider.birthday
    )

  def from(riderBike: RiderBike): RiderBikeResponse =
    RiderBikeResponse(
      riderBike.id,
      riderBike.riderId,
      riderBike.bikeId,
      riderBike.name,
      riderBike.maxSpeed,
      riderBike.cc
    )

  def from(riderBikeRequest: RiderBikeRequest): RiderBike =
    RiderBike(0L,
      riderBikeRequest.riderId,
      riderBikeRequest.bikeId,
      riderBikeRequest.name,
      riderBikeRequest.maxSpeed,
      riderBikeRequest.cc
    )
}
