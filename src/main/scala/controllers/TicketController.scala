package controllers

import io.finch.circe._
import io.finch.syntax._
import io.finch.{Application, Endpoint, jsonBody, path}
import models.JsonProtocol.Implicits.{ticketDataDecoder, ticketEncoder, ticketResultEncoder}
import models._
import services.TicketService
import services.TicketService.TicketNotFound

class TicketController(ticketService: TicketService) extends ControllerBase {

  def getTickets: Endpoint[TicketResult] = {
    get("api" :: "tickets" :: TicketFilter()) { filter: TicketFilterData =>
      ticketService.gets(filter).map { result =>
        returnWith(OK, result)
      }
    }
  }

  def getTicket: Endpoint[Ticket] = {
    get("api" :: "tickets" :: path[Long]) { id: Long =>
      ticketService.get(id).map {
        case Right(result)        => returnWith(OK, result)
        case Left(TicketNotFound) => returnWith(NotFound, TicketNotFound)
        case Left(e)              => returnWith(InternalServerError, e)
      }
    }
  }

  def createTicket: Endpoint[Long] = {
    post("api" :: "tickets" :: jsonBody[TicketData]) { ticketData: TicketData =>
      ticketService.create(ticketData).map { result =>
        returnWith(Created, result)
      }
    }
  }

  def updateTicket: Endpoint[Unit] = {
    put("api" :: "tickets" :: path[Long] :: jsonBody[TicketData]) { (id: Long, ticketData: TicketData) =>
      ticketService.update(id, ticketData).map {
        case true  => returnWith(OK)
        case false => returnWith(NotFound)
      }
    }
  }

  def deleteTicket: Endpoint[Unit] = {
    delete("api" :: "tickets" :: path[Long]) { id: Long =>
      ticketService.delete(id).map {
        case true  => returnWith(OK)
        case false => returnWith(NotFound, TicketNotFound)
      }
    }
  }

  val route = (getTickets :+: getTicket :+: createTicket :+: updateTicket :+: deleteTicket)
    .handle {
      case e: Exception => returnWith(InternalServerError, e)
    }
    .toServiceAs[Application.Json]

}
