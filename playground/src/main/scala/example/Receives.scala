package example

import akka.actor.Actor

case object StartImport
case object ImportDone

class BecomeActor extends Actor {
  override def receive: Receive = idle

  private[this] val idle: Receive = { case StartImport =>
    println("starting import")
    context.become(importing)
  }

  private[this] val importing: Receive = {
    case StartImport =>
      println("already busy, rejecting message")

    case ImportDone => context.become(idle)
  }
}
