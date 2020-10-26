import $ivy.`org.typelevel::cats-core:2.2.0`
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._ // don't do this!
import cats.data.OptionT

def abstractSequence(): Unit = {
  // Simplified: def sequence[A](xs: List[Future[A]]): Future[List[A]]
  Future.sequence(List(1, 2, 3, 4, 5).map(i => Future { println(s"$i") }))

  // what about:
  // Either.sequence(List(1, 2, 3, 4, 5).map(i => Right(s"$i")))
  // Option.sequence(List(1, 2, 3, 4, 5).map(i => Some(s"$i")))

  ()
}

def futureOption() = {
  def maybeFuture(i: Int): Future[Option[String]] = if (i % 2 == 0) {
    Future.successful(None)
  } else {
    Future { Some(s"$i is odd")}
  }

  // we only want to continue if it's a Future(Some(???))
  // not working:
  val onlyfor: Future[(Option[String], Option[String])] = for {
    v1 <- maybeFuture(1)
    v2 <- maybeFuture(3)
  } yield (v1, v2)
  // Future(Some("1 is odd"), Some("3 is odd"))

  val manual: Future[Option[(String, String)]] = maybeFuture(1).flatMap {
    case None => Future.successful(None)
    case Some(v1) => maybeFuture(3).flatMap {
      case None => Future.successful(None)
      case Some(v2) => Future.successful(Some((v1,v2)))
    }
  }
  // Future(Some(("1 is odd", "3 is odd")))

  // with OptionT:
  val transformer = {
    for {
      v1 <- OptionT(maybeFuture(1))
      v2 <- OptionT(maybeFuture(3))
    } yield (v1, v2)
  }.value
  // Future(Some(("1 is odd", "3 is odd")))

  Future.sequence(List(onlyfor, manual, transformer))
}

@main
def main(): Unit = {
    val List(onlyfor, manual, transformer) = Await.result(futureOption, Duration.Inf) // also don't do Inf...

  println(onlyfor)
  println(manual)
  println(transformer)
}
