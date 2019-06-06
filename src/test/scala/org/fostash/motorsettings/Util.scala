package org.fostash.motorsettings
import cats.effect.IO
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import org.http4s.{EntityDecoder, Response, Status}

trait Util {
  implicit val log = Slf4jLogger.unsafeCreate[IO]

  // Return true if match succeeds; otherwise false
  def check[A](actual:        IO[Response[IO]],
               expectedStatus: Status,
               expectedBody:   Option[A])(
                implicit ev: EntityDecoder[IO, A]
              ): Boolean =  {
    val actualResp         = actual.unsafeRunSync
    val statusCheck        = actualResp.status == expectedStatus
    val bodyCheck          = expectedBody.fold[Boolean](
      actualResp.body.compile.toVector.unsafeRunSync.isEmpty)( // Verify Response's body is empty.
      expected => actualResp.as[A].unsafeRunSync == expected
    )
    statusCheck && bodyCheck
  }
}
