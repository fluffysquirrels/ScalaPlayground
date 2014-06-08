package name.alex.sp.topTenMovies.naiveImpl

import com.github.nscala_time.time.Imports._
import name.alex.sp.topTenMovies.publicInterface._
import java.util.concurrent.atomic.AtomicLong
import scala.collection.concurrent.TrieMap
import scala.collection
import name.alex.sp.util.ConcurrentCountMap
import org.joda.time

class NaiveMoviePageViewsService extends MoviePageViewsService {
  private sealed case class MoviePageViewInPeriod(movieId: MovieId, periodStart: DateTime)

  private val movieCounts = new ConcurrentCountMap[MoviePageViewInPeriod]()

  override def countPageView(movieId: MovieId, viewTime: DateTime): Unit = {
    val viewInPeriod = MoviePageViewInPeriod(movieId, getViewTimePeriodStart(viewTime))
    movieCounts.add(viewInPeriod, 1)
  }

  override def getTopTenMoviesSoFarToday(now: DateTime): Vector[MovieCount] = {
    val today = now.withZone(time.DateTimeZone.UTC).withTimeAtStartOfDay()
    movieCounts.toIndexedSeq.filter {
      case (MoviePageViewInPeriod(_, viewTime), _) => viewTime == today
    }
    .sortBy {
      _ match {
        case (MoviePageViewInPeriod(_, _), count) => -count
      }
    }
    .take(10)
    .map {
      case ((MoviePageViewInPeriod(movieId, _), currCount)) => MovieCount(movieId, PageViewsCount(currCount))
    }
    .toVector
  }

  private def getViewTimePeriodStart(viewTime: DateTime): DateTime = {
    //viewTime.withTime(viewTime.hourOfDay(), viewTime.minuteOfHour(), secondOfMinute = 0, millisOfSecond = 0)
    viewTime.withZone(time.DateTimeZone.UTC).withTimeAtStartOfDay()
  }
}


