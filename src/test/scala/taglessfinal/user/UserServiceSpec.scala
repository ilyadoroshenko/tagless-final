package taglessfinal.user

import cats.data.{EitherT, OptionT}
import cats.effect.IO
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import taglessfinal.BaseSpec
import taglessfinal.reqres.{ReqResService, ReqResUser, ReqResUserNotFound}

class UserServiceSpec extends BaseSpec{

  ".create()" should {
    "return saved user" in new ctx {
      val req = UserRequest("ann.smith@email.com", 42)
      val reqResUser = ReqResUser(req.userId, "ann", "smith")
      val user = User(Some(1), req.email, reqResUser.firstName, reqResUser.lastName, req.userId)
      when(daoMock.save(any)).thenReturn(IO.pure(user))
      when(daoMock.getByEmail(req.email)).thenReturn(OptionT(IO.pure[Option[User]](None)))
      when(reqResServiceMock.getById(req.userId))
        .thenReturn(EitherT(IO.pure[Either[ReqResUserNotFound, ReqResUser]](Right(reqResUser))))

      val result = service.create(req).value.unsafeRunSync()

      result shouldBe Right(user)
    }

    "return AlreadyExists if dao indicates that user with the specified email exists" in new ctx {
      val req = UserRequest("ann.smith@email.com", 42)
      val user = User(Some(1), req.email, "ann", "smith", req.userId)
      when(daoMock.getByEmail(req.email)).thenReturn(OptionT(IO.pure[Option[User]](Some(user))))

      val result = service.create(req).value.unsafeRunSync()

      result shouldBe Left(AlreadyExists(req.email))
    }

    "return IdNotFound if ReqResService is unable to find one" in new ctx {
      val req = UserRequest("ann.smith@email.com", 42)
      when(daoMock.getByEmail(req.email)).thenReturn(OptionT(IO.pure[Option[User]](None)))
      when(reqResServiceMock.getById(req.userId))
        .thenReturn(EitherT(IO.pure[Either[ReqResUserNotFound, ReqResUser]](Left(ReqResUserNotFound(req.userId)))))

      val result = service.create(req).value.unsafeRunSync()

      result shouldBe Left(IdNotFound(req.userId))
    }
  }

  trait ctx {
    val daoMock = mock[UserDao[IO]]
    val reqResServiceMock = mock[ReqResService[IO]]
    val service = new DefaultUserService[IO](daoMock, reqResServiceMock)
  }
}
