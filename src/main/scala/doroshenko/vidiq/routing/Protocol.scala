package doroshenko.vidiq.routing

import io.circe.generic.extras.Configuration

object Protocol {
  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames
}
