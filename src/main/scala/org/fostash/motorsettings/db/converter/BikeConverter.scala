package org.fostash.motorsettings.db.converter
import org.fostash.motorsettings.db.entities.Bike
import org.fostash.motorsettings.repository.model.{BikeRequest, BikeResponse}

object BikeConverter {

  def from(bikeRequest: BikeRequest): Bike =
    Bike(0L,
      bikeRequest.brand,
      bikeRequest.cc)

  def from(bike: Bike): BikeResponse =
    BikeResponse(
      bike.id,
      bike.brand,
      bike.cc
    )

}
