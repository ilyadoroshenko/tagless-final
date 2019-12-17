package doroshenko.vidiq.reqres

import io.circe.generic.extras.ConfiguredJsonCodec
import doroshenko.vidiq.routing.Protocol._

@ConfiguredJsonCodec
case class ReqResUser(id: Long, firstName: String, lastName: String)

@ConfiguredJsonCodec
case class ReqResResponse(data: ReqResUser)
