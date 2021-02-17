package kz.asaheyt.inc.autopartsstore.http.routes

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Directives.{complete, concat, get, pathEndOrSingleSlash, pathPrefix}
import akka.http.scaladsl.server.Route
import com.typesafe.config.Config
import kz.asaheyt.inc.autopartsstore.http.model.AutoPartCommand

class HttpRoutes(databaseActor: ActorSystem[AutoPartCommand])(implicit system: ActorSystem[_], config: Config)  {

  val routes: Route = pathPrefix("home") {
    pathPrefix("test") {
      concat(
        pathEndOrSingleSlash {
          get {
            complete("Test! AutoPartsStore")
          }
        },
        new AutoPartRoutes(databaseActor).routes
      )
    }
  }
}
