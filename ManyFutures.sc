import scala.concurrent._

def manyFutures(implicit ec: ExecutionContext) = for {
  one   <- Future { 1 }
  two   <- Future { 2 }
  three <- Future { 3 }
  four  <- Future { 4 }
  five  <- Future { 5 }
} yield List(one, two, three, four, five)
