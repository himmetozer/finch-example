import com.twitter.finagle.Http
import com.twitter.server.TwitterServer
import com.twitter.util.Await
import com.typesafe.config.ConfigFactory
import controllers.TicketController
import repositories.{DBContext, TicketRepo}
import services.TicketService

object Application extends AppBase {

  val ticketRepo = new TicketRepo(db)
  val ticketService = new TicketService(ticketRepo)
  val ticketController = new TicketController(ticketService)

  val endpoints = ticketController.route

  def main() {
    val server = Http.serve(":8888", endpoints)
    onExit {
      server.close()
    }
    Await.ready(server)
  }
}

trait AppBase extends TwitterServer {
  lazy val config = ConfigFactory.load()
  lazy val db = new DBContext(config)
}
