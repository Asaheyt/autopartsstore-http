package kz.asaheyt.inc.autopartsstore.http.routes

import akka.Done
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout
import kz.asaheyt.inc.autopartsstore.http.model.{AddAutoPartCommand, AutoPart, AutoPartCommand, DeleteAutoPartCommand, EditAutoPartCommand, GetAutoPartCommand, GetAutoPartsCommand, Summary}
import kz.asaheyt.inc.autopartsstore.http.util.Codec

import scala.concurrent.Future
import scala.concurrent.duration._

class AutoPartRoutes(databaseActor: ActorSystem[AutoPartCommand])(implicit system: ActorSystem[_]) extends Codec with FailFastCirceSupport {

    implicit val timeout: Timeout = 3.seconds

    val routes: Route = {
      autoPartAddRoute ~ autoPartGetRoute ~ autoPartDeleteRoute ~ autoPartEditRoute ~ autoPartGetAllRoute
    }

    def autoPartAddRoute(): Route = {
      pathPrefix("autopart" / "add") {
        post {
          entity(as[AutoPart]) { entity =>
            val reply: Future[Summary] = databaseActor.ask(AddAutoPartCommand(entity, _))

            onComplete(reply) { summary =>
              complete(summary)
            }

          }
        }
      }
    }

    def autoPartEditRoute(): Route = {
      pathPrefix("autopart" / "edit") {
        put {
          entity(as[AutoPart]) { entity =>
            val reply: Future[Summary] = databaseActor.ask(EditAutoPartCommand(entity, _))

            onComplete(reply) { summary =>
              complete(summary)
            }

          }
        }
      }
    }

    def autoPartDeleteRoute(): Route = {
      pathPrefix("autopart" / "delete") {
        delete {
          parameter("autoPartId") {(autoPartId) =>

            val reply: Future[Done] = databaseActor.ask(DeleteAutoPartCommand(autoPartId.toLong, _))

            onComplete(reply) { summary =>
              complete(summary)
            }

          }
        }
      }
    }

    def autoPartGetRoute(): Route = {
      pathPrefix("autopart" / "get") {
        get {
          parameter("autoPartId") { (autoPartId) =>
            val reply: Future[Summary] = databaseActor.ask(GetAutoPartCommand(autoPartId.toLong, _))

            onComplete(reply) { summary =>
              complete(summary)


            }
          }
        }
      }

    }
    def autoPartGetAllRoute(): Route = {
      pathPrefix("autopart") {

        get {
          val reply: Future[List[Summary]] = databaseActor.ask(GetAutoPartsCommand(_))

          onComplete(reply) { summary =>
            complete(summary)

          }
        }
      }

    }
}
