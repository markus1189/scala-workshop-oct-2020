package example

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}

object HelloWorldExample {
  sealed abstract class StartHelloWorld
  final case object StartHelloWorld extends StartHelloWorld

  final case class Hello(replyTo: ActorRef[World])
  final case class World(sender: ActorRef[Hello])

  def sayHello(): Behavior[Hello] = Behaviors.receive { (context, message) =>
    context.log.info(s"Forwarding hello to ${message.replyTo}")
    message.replyTo ! World(context.self)
    Behaviors.same
  }

  def receiveWorld(): Behavior[World] = Behaviors.receive { (context, message) =>
    context.log.info(s"'Hello World' from {}", message.sender)
    Behaviors.same
  }

  def setup(): Behavior[StartHelloWorld] = Behaviors.setup { context =>
    val hello = context.spawn(sayHello(), "hello-actor")
    val world = context.spawn(receiveWorld(), "world-actor")

    Behaviors.receiveMessage { case StartHelloWorld =>
      hello ! Hello(world)
      Behaviors.stopped
    }
  }

  def main(args: Array[String]): Unit = {
    val system: ActorSystem[StartHelloWorld] = ActorSystem(setup(), "hello-world-system")

    system ! StartHelloWorld
  }
}