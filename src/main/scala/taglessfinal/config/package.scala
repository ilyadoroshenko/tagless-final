package taglessfinal

import io.circe.Decoder
import io.circe.generic.semiauto._
import org.http4s.circe._


package object config {
  implicit val scDec: Decoder[ServerConfig] = deriveDecoder
  implicit val dcDec: Decoder[DatabaseConfig] = deriveDecoder
  implicit val acDec: Decoder[AppConfig] = deriveDecoder
}
