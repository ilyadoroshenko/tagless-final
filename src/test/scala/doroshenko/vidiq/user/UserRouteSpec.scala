package doroshenko.vidiq.user

import cats.data.EitherT
import cats.effect.IO
import doroshenko.vidiq.BaseSpec
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.Method._
import org.http4s.Status._
import org.http4s.circe._
import org.http4s.implicits._
import org.http4s.{EntityDecoder, Request}
import org.mockito.Mockito._

class UserRouteSpec extends BaseSpec{

  "/users" when {
    "POST" should {
      "return created user and 201" in new ctx {
        implicit val userDecoder: EntityDecoder[IO, User] = jsonOf
        val req = UserRequest("ann@email.com", 42)
        val user = User(Some(1), req.email, "ann", "smith", req.userId)
        when(serviceMock.create(req)).thenReturn(EitherT(IO.pure[Either[UserCreationError, User]](Right(user))))

        val response = routes.run(Request[IO](POST, uri"/").withEntity(req.asJson)).unsafeRunSync()

        response.status shouldBe Created
        response.as[User].unsafeRunSync() shouldBe user
      }

      "return 404 if user with specified ID not found" in new ctx {
        implicit val userDecoder: EntityDecoder[IO, User] = jsonOf
        val req = UserRequest("ann@email.com", 42)
        when(serviceMock.create(req)).thenReturn(EitherT(IO.pure[Either[UserCreationError, User]](Left(IdNotFound(req.userId)))))

        val response = routes.run(Request[IO](POST, uri"/").withEntity(req.asJson)).unsafeRunSync()

        response.status shouldBe NotFound
      }

      "return 400 if user with specified email already exists" in new ctx {
        implicit val userDecoder: EntityDecoder[IO, User] = jsonOf
        val req = UserRequest("ann@email.com", 42)
        when(serviceMock.create(req)).thenReturn(EitherT(IO.pure[Either[UserCreationError, User]](Left(AlreadyExists(req.email)))))

        val response = routes.run(Request[IO](POST, uri"/").withEntity(req.asJson)).unsafeRunSync()

        response.status shouldBe BadRequest
      }
    }
  }


  trait ctx {
    val serviceMock = mock[UserService[IO]]
    val routes = (new UserRoute[IO](serviceMock)).route.orNotFound
  }
}
