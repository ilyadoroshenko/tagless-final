package doroshenko.vidiq.user

import io.circe.Decoder
import io.circe.generic.extras.ConfiguredJsonCodec
import io.circe.generic.semiauto._
import doroshenko.vidiq.routing.Protocol._

@ConfiguredJsonCodec
case class UserRequest(email: String, userId: Long)


@ConfiguredJsonCodec
case class User(id: Option[Long] = None, email: String, firstName: String, lastName: String, referenceId: Long)

