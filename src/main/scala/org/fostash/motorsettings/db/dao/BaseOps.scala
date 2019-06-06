package org.fostash.motorsettings.db.dao

import cats.effect._
import cats.implicits._
import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.transactor.Transactor
import doobie.util.update.Update0
import fs2.Stream

trait BaseOps[F[_]] {

  def transactor: Transactor[F]

  def insert(statement: Update0)(implicit w: Bracket[F, Throwable]): F[Long] =
    statement
      .withUniqueGeneratedKeys[Long]("id")
      .transact(transactor)

  def read[A, B](query: Query0[A], converter: A => B)(implicit w: Bracket[F, Throwable]): F[Either[EntityNotFoundError.type, B]] =
    query.option.transact(transactor).map {
      case Some(entity) => Right(converter(entity))
      case None => Left(EntityNotFoundError)
    }

  def stream[A, B](query: Query0[A], converter: A => B)(implicit w: Bracket[F, Throwable]): Stream[F, B] =
    query.map(converter).stream.transact(transactor)

  def updelete(query: Update0)(implicit w: Bracket[F, Throwable]): F[Either[EntityNotFoundError.type, Boolean]] =
    query
      .run
      .transact(transactor)
      .map { affectedRows =>
        if (affectedRows == 1) {
          Right(true)
        } else {
          Left(EntityNotFoundError)
        }
      }
}

case object EntityNotFoundError