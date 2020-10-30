package example

import java.util.concurrent.Executors

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future, blocking}

object Futures extends App {
  def makeFuture(i: Int)(implicit ec: ExecutionContext): Future[Int] = {
    if (i == 42) {
      Future.failed(throw new RuntimeException("oops"))
    } else
      Future {
        Thread.sleep(1000)
        println(s"The number is ${i}")
        i
      }
  }

  {
    implicit val ec: ExecutionContext = ExecutionContext.global

    val result = for {
      List(x, z) <- Future.traverse(List(1, 2))(i => makeFuture(i))
      y <- makeFuture(42)
    } yield x

    Await.result(result, Duration.Inf)
  }
}

class UserRepository(val blockingExecutionContext: ExecutionContext) {

  val ids = List.fill(1000)(42)
  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  import scala.concurrent.duration._

  def apiCall(i: Int): Future[Unit] = Future.successful(())

  val result = Source(ids)
    .mapAsyncUnordered(5)(id => apiCall(id))
    .runWith(Sink.foreach(println(_)))
}
