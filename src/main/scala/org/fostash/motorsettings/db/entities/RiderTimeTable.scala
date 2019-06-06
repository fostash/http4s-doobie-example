package org.fostash.motorsettings.db.entities

case class RiderTimeTable(id: Long, circuitId: Long, riderId: Long, bikeId: Long, sessionDate: String)
case class RiderLap(id: Long, timeTableId: Long, lapNum: Int, minutes: Int, seconds: Int, millis: Int)