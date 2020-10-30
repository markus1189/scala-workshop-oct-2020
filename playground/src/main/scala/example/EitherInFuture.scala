package example

import monix.eval.Task

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object EitherInFuture {
  import scala.concurrent.ExecutionContext.Implicits.global

  case class CarTicket(number: Int)

  sealed trait CarError extends Serializable with Product
  case object NoCarWithThisColor extends CarError
  case object OutOfServiceHours extends CarError

  case class CarErrorOther(error: OtherError) extends CarError

  sealed trait OtherError
  case object SomeOtherError extends OtherError

  def bookCar(color: String): Future[Either[CarError, CarTicket]] = {
    for {
      car <- getCarFromDb()
      isAvailable <- callExternalService()
    } yield {
      if (!isAvailable) {
        Left(NoCarWithThisColor)
      } else {
        Right(car)
      }
    }
  }

  for {
    x <- List(1,2,3)
    y <- List(4,5)
  } yield x + y

  val result = bookCar("green").map {
    case Left(NoCarWithThisColor) => ???
    case Left(OutOfServiceHours)  => ???

    case Right(value) => value
  }

  val f1: Future[Nothing] => Future[Nothing] = akka.pattern.after(5.seconds)

  Future.firstCompletedOf(f1, Future { })
}
