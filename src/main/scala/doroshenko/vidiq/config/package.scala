package doroshenko.vidiq

import io.circe.Decoder
import io.circe.generic.semiauto._


package object config {
  implicit val scDec: Decoder[ServerConfig] = deriveDecoder
  implicit val dcDec: Decoder[DatabaseConfig] = deriveDecoder
  implicit val acDec: Decoder[AppConfig] = deriveDecoder
}
