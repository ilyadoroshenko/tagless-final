package doroshenko.vidiq.user

import cats.data.OptionT
import cats.effect.Bracket
import cats.implicits._
import doobie.implicits._
import doobie.util.query.Query0
import doobie.{Transactor, Update0}


class PostgresUserDao[F[_]: Bracket[*[_], Throwable]](
  val xa: Transactor[F]
) extends UserDao[F] {

  def insert(user: User): Update0 = sql"""
    INSERT INTO USERS (email, reference_id, first_name, last_name)
    VALUES (${user.email}, ${user.referenceId}, ${user.firstName}, ${user.lastName})
  """.update

  def byEmail(email: String): Query0[User] = sql"""
    SELECT id, email, first_name, last_name, reference_id
    FROM users
    WHERE email = $email
  """.query[User]

  def deleteQuery(email: String): Update0 = sql"""
    DELETE FROM users WHERE email = $email
  """.update


  override def save(user: User): F[User] = {
    insert(user).withUniqueGeneratedKeys[Long]("id").map(id => user.copy(id = id.some)).transact(xa)
  }

  override def getByEmail(email: String): OptionT[F, User] = OptionT(byEmail(email).option.transact(xa))

  override def delete(email: String): OptionT[F, Unit] = {
    OptionT(deleteQuery(email).run.transact(xa).map(rows => if (rows == 1) Some(()) else None))
  }
}
