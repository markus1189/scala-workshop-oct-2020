package example

import akka.actor.Actor
import akka.actor.Status.{Failure, Success}

import scala.concurrent.{ExecutionContext, Future}
import akka.pattern.pipe

class MyActor extends Actor {
  implicit val ec = context.dispatcher

  case class Message(answer: Int)
  case class FutureDone(result: Unit)

  override def receive: Receive = {
    case Success(FutureDone) =>
    case Failure(e)          => e
    case Message(answer) =>
      val replyTo = sender()

      Future { Thread.sleep(1000) }
        .map {
          replyTo
          ()
        }
        .map(FutureDone)
        .pipeTo(self)

      ()
  }
}
