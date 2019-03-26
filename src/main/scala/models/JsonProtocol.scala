package models

import io.circe._
import io.circe.generic.semiauto._

object JsonProtocol {

  object Implicits {

    implicit val ticketDataDecoder: Decoder[TicketData] = deriveDecoder

    implicit val ticketEncoder: Encoder[Ticket] = deriveEncoder
    implicit val ticketResultEncoder: Encoder[TicketResult] = deriveEncoder
    implicit val messageEncoder: Encoder[Message] = deriveEncoder

  }

}
