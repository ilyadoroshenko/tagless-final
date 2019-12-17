package doroshenko.vidiq.reqres

sealed trait ReqResError

case class ReqResUserNotFound(id: Long) extends ReqResError
