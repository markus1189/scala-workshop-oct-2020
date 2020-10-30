package example

import scala.concurrent.ExecutionContext

class MockitoExample {
  implicit val ec = ExecutionContext.global

  def future(i: Int)(implicit ec: ExecutionContext) = ???

  def any() = ???

  when { future(any()) } returns ???
}
