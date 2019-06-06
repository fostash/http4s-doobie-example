package org.fostash.motorsettings

import com.typesafe.config.ConfigFactory

object Config {

  private val config = ConfigFactory.load()

  lazy val Host: String = config.getString("host")
  lazy val Port: Int = config.getInt("port")

  lazy val DatabaseUrl: String = config.getString("mysql.properties.url")
  lazy val DatabaseUsername: String = config.getString("mysql.properties.user")
  lazy val DatabasePassword: String = config.getString("mysql.properties.password")
  lazy val DatabaseDriver: String = config.getString("mysql.properties.driver")
}
