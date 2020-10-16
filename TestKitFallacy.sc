import $ivy.`com.typesafe.akka::akka-actor:2.6.10`
import $ivy.`com.typesafe.akka::akka-testkit:2.6.10`
import $ivy.`org.scalatest::scalatest:3.2.2`

import akka.actor.{ActorSystem, Actor, Props}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class EchoActor extends Actor {
  override def receive = {
    case message => sender() ! message
  }
}

class TestKitFallacySpec
    extends TestKit(ActorSystem("TestKitFallacy"))
    with ImplicitSender
    with AnyWordSpecLike {

  "Demonstration of TestKit gotcha" must {
    "first test" in {
      val echo = system.actorOf(Props[EchoActor]())

      echo ! "hello world"
      echo ! "the second message"

      expectMsg("hello world")
    }

    "second test" in {
      val echo = system.actorOf(Props[EchoActor]())

      echo ! "expected message"

      expectMsg("expected message")
    }

  }
}

@main
def main(): Unit = {
  (new TestKitFallacySpec).execute()
}
