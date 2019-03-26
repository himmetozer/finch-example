package controllers

import com.twitter.finagle.http.Status
import io.circe.Encoder
import io.finch.Output

trait ControllerBase {

  val OK = Status.Ok
  val Created = Status.Created
  val NotFound = Status.NotFound
  val BadRequest = Status.BadRequest
  val InternalServerError = Status.InternalServerError

  def returnWith[T: Encoder](statusCode: Status, body: T): Output[T] = {
    Output.payload(body, statusCode)
  }

  def returnWith(statusCode: Status): Output[Unit] = {
    Output.empty(statusCode)
  }

  def returnWith[T](statusCode: Status, e: Exception): Output[T] = {
    Output.failure(e, statusCode)
  }

}
