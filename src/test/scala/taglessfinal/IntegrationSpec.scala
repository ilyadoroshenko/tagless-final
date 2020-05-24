package taglessfinal

import cats.effect.{Blocker, IO}
import doobie.implicits._
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import io.circe.config._
import org.scalatest.BeforeAndAfterEach
import taglessfinal.config.DatabaseConfig

trait IntegrationSpec extends BaseSpec with BeforeAndAfterEach {
  lazy val cfg = parser.decodePath[DatabaseConfig]("test.db").getOrElse(throw new RuntimeException("invalid test config"))
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)
  lazy val xa = Transactor.fromDriverManager[IO](
    cfg.driver,
    cfg.url,
    cfg.user,
    cfg.password,
    Blocker.liftExecutionContext(ExecutionContexts.synchronous)
  )

  override protected def afterEach(): Unit = {
    super.afterEach()
    sql"truncate users".update.run.transact(xa).unsafeRunSync()
  }
}
