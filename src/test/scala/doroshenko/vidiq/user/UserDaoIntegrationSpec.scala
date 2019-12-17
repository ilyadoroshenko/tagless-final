package doroshenko.vidiq.user

import cats.effect.IO
import doroshenko.vidiq.IntegrationSpec

class UserDaoIntegrationSpec extends IntegrationSpec {

  ".create()" should {
    "return user with populated id field" in new ctx {
      val user = User(None, "ann20@mail.com", "ann", "smith", 42)

      val savedUser = dao.save(user).unsafeRunSync()

      savedUser.id.isDefined shouldBe true
      savedUser shouldEqual user.copy(id = savedUser.id)
    }

    "persist user for further queries" in new ctx {
      val user = User(None, "ann30@mail.com", "ann", "smith", 42)

      val savedUser = dao.save(user).unsafeRunSync()
      val fetchedUser = dao.getByEmail(user.email).value.unsafeRunSync()

      fetchedUser shouldEqual Some(savedUser)
    }
  }

  trait ctx {
    val dao = new PostgresUserDao[IO](xa)
  }
}
