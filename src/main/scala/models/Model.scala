package models

import io.finch.Endpoint

case class Message(message: String)

case class Ticket(id: Long, title: String)

case class TicketData(title: String) {
  require(title.length < 256, "OMG")
  require(title.nonEmpty, "title cannot be empty")

  def getTicket(id: Long = -1L) = Ticket(id, title)
}

case class TicketFilterData(startId: Option[Long], length: Int) {
  require(length < 101, "length can not be bigger than 100")
  require(length > 1, "length can not be smaller than 1")
}

case class TicketResult(nextPageStartId: Option[Long], tickets: List[Ticket])

object TicketFilter {
  import io.finch.{param, paramOption}

  def apply(): Endpoint[TicketFilterData] = (paramOption[Long]("startId") :: param[Int]("length")).as[TicketFilterData]
}
