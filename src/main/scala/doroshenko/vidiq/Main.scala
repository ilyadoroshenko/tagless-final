package doroshenko.vidiq

import cats.effect.{ExitCode, IO, IOApp, _}
import cats.implicits._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import doroshenko.vidiq.config.AppConfig
import doroshenko.vidiq.reqres.HttpReqResService
import doroshenko.vidiq.user.{DefaultUserService, PostgresUserDao, UserRoute}
import io.circe.config._
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import org.http4s.server.blaze._
import org.http4s.server.{Router, Server}

import scala.concurrent.ExecutionContext.global

object Main extends IOApp{

  def createServer[F[_]: ContextShift: ConcurrentEffect: Timer]: Resource[F, Server[F]] = {
    for {
      conf <- Resource.liftF(parser.decodePathF[F, AppConfig]("app"))
      connEc <- ExecutionContexts.fixedThreadPool[F](conf.db.poolSize)
      txnEc <- ExecutionContexts.cachedThreadPool[F]
      xa <- HikariTransactor.newHikariTransactor[F](
        conf.db.driver, conf.db.url, conf.db.user, conf.db.password, connEc, Blocker.liftExecutionContext(txnEc))
      httpClient <- BlazeClientBuilder(global).resource

      reqResService = new HttpReqResService(httpClient)
      userDao = new PostgresUserDao(xa)
      userService = new DefaultUserService(userDao, reqResService)
      userRoute = new UserRoute(userService)

      httpApp = Router(
        "/users" -> userRoute.route,
      ).orNotFound

      server <- BlazeServerBuilder[F]
        .bindHttp(conf.server.port, conf.server.host)
        .withHttpApp(httpApp)
        .resource
    } yield server

  }

  override def run(args: List[String]): IO[ExitCode] = createServer.use(_ => IO.never).as(ExitCode.Success)

}
