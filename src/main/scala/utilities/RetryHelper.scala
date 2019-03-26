package utilities

import com.carrambata.retry4s.{Backoff, FailRetryStats, Future, SuccessRetryStats}
import com.twitter.finagle.stats.Stat
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.ExecutionContext

trait RetryHelper {
  this: StrictLogging =>

  import com.carrambata.retry4s.Success.Implicits.all

  object Retry {
    def apply[T](timer: Stat, logData: String, count: Int = 3)(promise: () => Future[T])(
      implicit executionContext: ExecutionContext): Future[T] = {
      Backoff(3)(promise) {
        case FailRetryStats(duration, 0) =>
          logger.error(s"Failed to $logData")
          timer.add(duration)
        case FailRetryStats(duration, remainingCount) =>
          logger.warn(s"Failed to get tickets for $logData, it will try $remainingCount times")
          timer.add(duration)
        case SuccessRetryStats(duration) => timer.add(duration)
      }
    }
  }
}
