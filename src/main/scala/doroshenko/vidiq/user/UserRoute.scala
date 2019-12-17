package doroshenko.vidiq.user

import cats.effect.Sync
import cats.implicits._
import doroshenko.vidiq.routing.ErrorResponse
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.{EntityDecoder, HttpRoutes, Response}


class UserRoute[F[_]: Sync](
  userService: UserService[F]
) extends Http4sDsl[F] {

  private implicit val decoder: EntityDecoder[F, UserRequest] = jsonOf

  private def handleError(error: UserError): F[Response[F]] = error match {
    case EmailNotFound(email) => NotFound(ErrorResponse(s"User with email $email not found").asJson)
    case AlreadyExists(email) => BadRequest(ErrorResponse(s"User with email $email already exists").asJson)
    case IdNotFound(id) => NotFound(ErrorResponse(s"User with id $id not found").asJson)
  }

  def route: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root =>
      val res = for {
        user <- req.as[UserRequest]
        r <- userService.create(user).value
      } yield r

      res.flatMap {
        case Right(user) => Created(user.asJson)
        case Left(error) => handleError(error)
      }

    case GET -> Root / email =>
      userService.getByEmail(email).value.flatMap {
        case Right(user) => Ok(user.asJson)
        case Left(err) => handleError(err)
      }

    case DELETE -> Root / email =>
      userService.delete(email).value.flatMap {
        case Right(()) => NoContent()
        case Left(err) => handleError(err)
      }
  }
}
