package name.alex.sp.topTenMovies.inMemoryImpl

import com.github.nscala_time.time.Imports._
import name.alex.sp.topTenMovies.publicInterface._
import java.util.concurrent.atomic.AtomicLong
import scala.collection.concurrent.TrieMap
import scala.collection
import name.alex.sp.util.ConcurrentCountMap
import org.joda.time

import scala.concurrent.Future

class InMemoryMoviePageViewsService extends MoviePageViewsService {
  private sealed case class MoviePageViewInPeriod(movieId: MovieId, periodStart: DateTime)

  private val movieCounts = new ConcurrentCountMap[MoviePageViewInPeriod]()

  override def countPageView(movieId: MovieId, viewTime: DateTime): Future[Unit] = {
    val viewInPeriod = MoviePageViewInPeriod(movieId, getViewTimePeriodStart(viewTime))
    movieCounts.add(viewInPeriod, 1)
    return Future.successful(Unit)
  }

  override def getTopTenMoviesSoFarToday(now: DateTime): Future[Vector[MovieCount]] = {
    val today = now.withZone(time.DateTimeZone.UTC).withTimeAtStartOfDay()
    val rv = movieCounts.toIndexedSeq.filter {
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

    // TODO: Maybe use actual asynchrony
    return Future.successful(rv)
  }

  private def getViewTimePeriodStart(viewTime: DateTime): DateTime = {
    viewTime.withZone(time.DateTimeZone.UTC).withTimeAtStartOfDay()
  }

  override def close(): Unit = {
    // No-op as this is an in-memory service.
  }
}



