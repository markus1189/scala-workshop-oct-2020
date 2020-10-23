import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._ // don't do this!

val clock = java.time.Clock.systemDefaultZone

def launchMissile(number: Int): Future[Unit] = Future {
  if (number > 5) throw new RuntimeException("We only have 5 missiles")
  Thread.sleep(3000)
  println(s"${clock.instant}: Launching Missile #${number}")  
}

// we want to execute this in parallel...
def program1(): Unit = {
  val endOfWorld = for {
    _ <- launchMissile(1) 
    _ <- launchMissile(2) 
  } yield ()

  Await.result(endOfWorld, Duration.Inf)
}

// first try: pull out vals
def program2(): Unit = {
  val missile1 = launchMissile(1)  
  val missile2 = launchMissile(2)  

  val endOfWorld = for {
    _ <- missile1
    _ <- missile2
  } yield ()

  Await.result(endOfWorld, Duration.Inf)
}

// second try: use zip
def program3(): Unit = {
  val missile1 = launchMissile(1)  
  val missile2 = launchMissile(2)  

  val endOfWorld = missile1.zip(missile2).map(_ => ())

  Await.result(endOfWorld, Duration.Inf)
}

@main
def main(): Unit = {
  println("Program 1")
  program1()

  println("\nProgram 2")
  program2()

  println("\nProgram 3")
  program3()
}

// Output:
// 
// Program 1
// 2020-10-23T12:24:11.700057Z: Launching Missile #1
// 2020-10-23T12:24:14.702868Z: Launching Missile #2
// 
// Program 2
// 2020-10-23T12:24:17.704146Z: Launching Missile #2
// 2020-10-23T12:24:17.704146Z: Launching Missile #1
// 
// Program 3
// 2020-10-23T12:24:20.705161Z: Launching Missile #2
// 2020-10-23T12:24:20.705161Z: Launching Missile #1
