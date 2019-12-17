package doroshenko.vidiq.user

import cats.data.OptionT

trait UserDao[F[_]] {
  def getByEmail(email: String): OptionT[F, User]

  def save(reqResUser: User): F[User]

  def delete(email: String): OptionT[F, Unit]
}
