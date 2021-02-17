package kz.asaheyt.inc.autopartsstore.http

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import com.typesafe.config.{Config, ConfigFactory}
import kz.asaheyt.inc.autopartsstore.http.actor.DatabaseActor
import kz.asaheyt.inc.autopartsstore.http.database.Database
import kz.asaheyt.inc.autopartsstore.http.model.AutoPartCommand
import kz.asaheyt.inc.autopartsstore.http.routes.{AutoPartRoutes, HttpRoutes}
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object Boot extends App {
  implicit val config: Config = ConfigFactory.load()

  val log = LoggerFactory.getLogger(getClass)

  implicit val actorSystem = ActorSystem(Behaviors.empty, config.getString("akka.actor.system"))

  implicit def executionContext: ExecutionContext = actorSystem.executionContext

  val database = new Database()

  val databaseActor: ActorSystem[AutoPartCommand] = ActorSystem(DatabaseActor(database), "database")

  val httpRoutes = new AutoPartRoutes(databaseActor)


  runHttpServer() onComplete {
    case Success(serverBinding) =>
      log.info(s"ServiceName http server has been started. $serverBinding")

    case Failure(exception) =>
      throw exception
  }

  private def runHttpServer(): Future[Http.ServerBinding] = {
    Http().newServerAt(config.getString("http-server.interface"), port = config.getInt("http-server.port")).bind(httpRoutes.routes)
  }

}
