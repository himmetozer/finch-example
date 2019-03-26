package services

import com.twitter.util.Future
import models.{Ticket, TicketData, TicketFilterData, TicketResult}
import repositories.TicketRepo

import scala.util.control.NoStackTrace

class TicketService(ticketRepo: TicketRepo) {

  import TicketService._

  def gets(filter: TicketFilterData): Future[TicketResult] = {
    ticketRepo.get(filter.startId, filter.length + 1).map { tickets =>
      if (tickets.size == filter.length + 1) {
        TicketResult(tickets.lastOption.map(_.id), tickets.dropRight(1))
      } else {
        TicketResult(None, tickets)
      }
    }
  }

  def get(id: Long): Future[Either[Exception, Ticket]] = {
    ticketRepo.getById(id).map {
      case None => Left(TicketNotFound)
      case Some(ticket) => Right(ticket)
    }
  }

  def create(ticketData: TicketData): Future[Long] = {
    ticketRepo.save(ticketData)
  }

  def update(id: Long, ticketData: TicketData): Future[Boolean] = {
    ticketRepo.update(id, ticketData).map(_ > 0)
  }

  def delete(id: Long): Future[Boolean] = {
    ticketRepo.delete(id).map(_ > 0)
  }

}

object TicketService {
  object TicketNotFound extends Exception with NoStackTrace
}
