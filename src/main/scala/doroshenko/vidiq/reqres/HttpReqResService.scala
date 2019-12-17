package doroshenko.vidiq.reqres


import cats.data.{EitherT, OptionT}
import cats.effect.{Resource, Sync}
import cats.implicits._
import io.circe.generic.auto._
import org.http4s.Method._
import org.http4s.circe.jsonOf
import org.http4s.client.Client
import org.http4s.{EntityDecoder, Request, Uri}


class HttpReqResService[F[_]: Sync](
  client: Client[F]
) extends ReqResService[F] {

  private implicit val decoder: EntityDecoder[F, ReqResResponse] = jsonOf

  override def getById(id: Long): EitherT[F, ReqResUserNotFound, ReqResUser] = {
    val req: Request[F] = Request(GET, Uri.fromString(s"https://reqres.in/api/users/$id").getOrElse(???))

    val res = client.expectOption[ReqResResponse](req).map(_.map(_.data))

    OptionT(res).toRight(ReqResUserNotFound(id))
  }
}
