package org.fostash.motorsettings
import cats.effect.Resource
import doobie.hikari.HikariTransactor

trait Database[F[_]] {

  def transactor(): Resource[F, HikariTransactor[F]]

  def migrate(transactor: HikariTransactor[F]): F[Unit]
}