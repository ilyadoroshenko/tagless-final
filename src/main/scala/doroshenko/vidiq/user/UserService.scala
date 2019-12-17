package doroshenko.vidiq.user

import cats.data.EitherT

trait UserService[F[_]] {
  def create(user: UserRequest): EitherT[F, UserCreationError, User]

  def delete(email: String): EitherT[F, EmailNotFound, Unit]

  def getByEmail(email: String): EitherT[F, EmailNotFound, User]
}
