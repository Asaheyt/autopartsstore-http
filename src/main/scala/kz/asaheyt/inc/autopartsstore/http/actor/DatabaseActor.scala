package kz.asaheyt.inc.autopartsstore.http.actor

import akka.Done
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import kz.asaheyt.inc.autopartsstore.http.database.Database
import kz.asaheyt.inc.autopartsstore.http.model.{AddAutoPartCommand, AutoPart, AutoPartCommand, DeleteAutoPartCommand, EditAutoPartCommand, GetAutoPartCommand, GetAutoPartsCommand, Summary}

import scala.List
import scala.collection.mutable.ArrayBuffer

object DatabaseActor {

  def apply(database: Database): Behavior[AutoPartCommand] = {
    Behaviors.receive { (context, message) =>
      message match {
        case cmd: AddAutoPartCommand =>
          println("Add " + cmd)
          cmd.replyTo ! Summary(database.addAutoPart(cmd.autoPart))
          Behaviors.same

        case cmd: EditAutoPartCommand =>
          println("Edit " + cmd)
          cmd.replyTo ! Summary(database.editAutoPart(cmd.autoPart))
          Behaviors.same

        case cmd: DeleteAutoPartCommand =>
          println("Delete " + cmd)
          cmd.replyTo ! {
            if(database.deleteAutoPart(cmd.autoPartId))
              Done
            else {
              //throw Exception
              Done
            }
          }
          Behaviors.same

        case cmd: GetAutoPartCommand =>
          println("Get " + cmd)
          cmd.replyTo ! Summary(database.getAutoPart(cmd.autoPartId))
          Behaviors.same

        case cmd: GetAutoPartsCommand =>
          println("GetAll" + cmd)
          val a : List[AutoPart] = database.getAutoParts().toList
          cmd.replyTo ! (for(e <- a) yield Summary(e))
          Behaviors.same
      }

    }
  }
}
