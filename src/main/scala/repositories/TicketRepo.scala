package repositories

import com.twitter.finagle.stats.LoadedStatsReceiver
import com.twitter.util.Future
import com.typesafe.scalalogging.StrictLogging
import models.{Ticket, TicketData}
import utilities.RetryHelper
import utilities.TwitterConverters.scalaToTwitterFuture

import scala.concurrent.ExecutionContext.Implicits.global

class TicketRepo(dBContext: DBContext) extends RetryHelper with StrictLogging {

  import TicketRepo._
  import dBContext._

  private val tickets = dBContext.quote(querySchema[Ticket]("ticket", _.id -> "id", _.title -> "title"))

  def get(maybeStartId: Option[Long], length: Int): Future[List[Ticket]] = {
    val query = maybeStartId match {
      case Some(startId) => quote(tickets.filter(_.id >= lift(startId)).take(lift(length)))
      case None          => quote(tickets.take(lift(length)))
    }

    Retry(getTicketsTimer, s"get tickets for startId:$maybeStartId, length: $length")(() => run(query))
  }

  def getById(id: Long): Future[Option[Ticket]] = {
    val query = quote(tickets.filter(_.id == lift(id)))

    Retry(getTicketsTimer, s"get ticket by for startId:$id")(() => run(query).map(_.headOption))
  }

  def save(ticketData: TicketData): Future[Long] = {
    val query = quote(tickets.insert(lift(ticketData.getTicket())).returning(_.id))

    Retry(saveTicketTimer, s"save ticket for data:${ticketData.toString}")(() => run(query))
  }

  def update(id: Long, ticketData: TicketData): Future[Long] = {
    val ticket = ticketData.getTicket(id)
    val query = quote(tickets.filter(_.id == lift(ticket.id)).update(lift(ticket)))

    Retry(updateTicketTimer, s"update ticket for data:${ticketData.toString}")(() => run(query))
  }

  def delete(id: Long): Future[Long] = {
    val query = quote(tickets.filter(_.id == lift(id)).delete)

    Retry(updateTicketTimer, s"delete ticket for id:$id")(() => run(query))
  }

}

object TicketRepo {
  val metrics = LoadedStatsReceiver.scope("ticketRepo")

  val getTicketsTimer = metrics.stat("getTickets")
  val getTicketTimer = metrics.stat("getTicket")
  val saveTicketTimer = metrics.stat("saveTicket")
  val updateTicketTimer = metrics.stat("updateTicket")
  val deleteTicketTimer = metrics.stat("deleteTicket")

}
