package example

object Monads {

  trait Monad[F[_]] {
    def apply[A](x: A): F[A]
    def flatMap[A,B](fa: F[A])(f: A => F[B]): F[B]
  }
}
