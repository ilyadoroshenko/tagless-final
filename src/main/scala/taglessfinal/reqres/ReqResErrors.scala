package taglessfinal.reqres

sealed trait ReqResError

case class ReqResUserNotFound(id: Long) extends ReqResError
