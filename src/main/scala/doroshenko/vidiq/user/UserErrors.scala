package doroshenko.vidiq.user

sealed trait UserError
case class EmailNotFound(email: String) extends UserError

sealed trait UserCreationError extends UserError
case class IdNotFound(id: Long) extends UserCreationError
case class AlreadyExists(email: String) extends UserCreationError
