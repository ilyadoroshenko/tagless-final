package doroshenko.vidiq.reqres

import cats.data.EitherT

trait ReqResService[F[_]] {
  def getById(id: Long): EitherT[F, ReqResUserNotFound, ReqResUser]
}
