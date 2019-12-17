package doroshenko.vidiq.user

import cats._
import cats.data.EitherT
import doroshenko.vidiq.reqres.{ReqResService, ReqResUser}

class DefaultUserService[F[_]: Monad](
  val dao: UserDao[F],
  val reqResService: ReqResService[F]
) extends UserService[F] {

  override def create(user: UserRequest): EitherT[F, UserCreationError, User] = {
    for {
      _ <- doesNotExist(user.email)
      fetchedUser <- reqResService.getById(user.userId).leftMap(_ => IdNotFound(user.userId))
      user <- EitherT.right(dao.save(mkUser(fetchedUser, user)))
    } yield user
  }

  override def delete(email: String): EitherT[F, EmailNotFound, Unit] = {
    dao.delete(email).toRight(EmailNotFound(email))
  }

  override def getByEmail(email: String): EitherT[F, EmailNotFound, User] = {
    dao.getByEmail(email).toRight(EmailNotFound(email))
  }

  private def doesNotExist(email: String): EitherT[F, AlreadyExists, Unit] = {
    dao.getByEmail(email).map(_ => AlreadyExists(email)).toLeft(())
  }

  private def mkUser(reqResUser: ReqResUser, userRequest: UserRequest): User = {
    User(None, userRequest.email, reqResUser.firstName, reqResUser.lastName, userRequest.userId)
  }
}






