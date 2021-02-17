package kz.asaheyt.inc.autopartsstore.http.model

import akka.Done
import akka.actor.typed.ActorRef

trait AutoPartCommand

case class AddAutoPartCommand(autoPart: AutoPart, replyTo: ActorRef[Summary]) extends AutoPartCommand

case class EditAutoPartCommand(autoPart: AutoPart, replyTo: ActorRef[Summary]) extends AutoPartCommand

case class DeleteAutoPartCommand(autoPartId: Long, replyTo: ActorRef[Done]) extends AutoPartCommand

case class GetAutoPartCommand(autoPartId: Long, replyTo: ActorRef[Summary]) extends AutoPartCommand

case class GetAutoPartsCommand(replyTo: ActorRef[List[Summary]]) extends AutoPartCommand