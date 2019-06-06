package org.fostash.motorsettings.db.entities

case class RiderBike(id: Long, riderId: Long, bikeId: Long, name: String, maxSpeed: BigDecimal, cc: Double)