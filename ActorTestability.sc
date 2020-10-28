import $ivy.`com.typesafe.akka::akka-actor:2.6.10`

import akka.actor.{Actor, ActorRef, Props, ActorRefFactory}

class ParentActor extends Actor {
  private[this] val child = context.actorOf(ChildActor.props())
  override def receive = {
    case _ => ()
  }
}

object ParentActor {
  def props: Props = Props(new ParentActor)
}

class ChildActor extends Actor {
  override def receive = {
    case _ => ()
  }
}

object ChildActor {
  def props(): Props = Props(new ChildActor)
}

// Better:
class ParentActorTestable(createChild: ActorRefFactory => ActorRef) extends Actor {
  private[this] val child = createChild(context)

  override def receive = {
    case _ => ()
  }
}

object ParentActorTestable {
  def props(createChild: ActorRefFactory => ActorRef = _.actorOf(ChildActor.props())) = {
    Props(new ParentActorTestable(createChild))
  }
}
