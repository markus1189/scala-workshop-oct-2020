import $ivy.`com.typesafe.akka::akka-actor:2.6.10`
import $ivy.`com.typesafe.akka::akka-testkit:2.6.10`
import $ivy.`org.scalatest::scalatest:3.2.2`

import akka.actor.{ActorSystem, Actor, Props}
import akka.testkit.TestProbe
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class EchoActor extends Actor {
  override def receive = {
    case message => sender() ! message
  }
}

class TestProbesSpec extends AnyWordSpecLike {

  private[this] implicit val system = ActorSystem() // Not shown: shutdown after tests!

  "Demonstration of TestProbes" must {
    "first test" in {
      val sender = TestProbe()
      implicit val senderRef = sender.ref
      val echo = system.actorOf(Props[EchoActor]())

      echo ! "hello world"
      echo ! "the second message"

      sender.expectMsg("hello world")
    }

    "second test" in {
      val sender = TestProbe()
      implicit val senderRef = sender.ref
      val echo = system.actorOf(Props[EchoActor]())

      echo ! "expected message"

      sender.expectMsg("expected message")
    }

  }
}

@main
def main(): Unit = {
  (new TestProbesSpec).execute()
}
