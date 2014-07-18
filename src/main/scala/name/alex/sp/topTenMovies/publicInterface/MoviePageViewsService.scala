package name.alex.sp.topTenMovies.publicInterface

import com.github.nscala_time.time.Imports._

import scala.concurrent.Future

trait MoviePageViewsService extends AutoCloseable {
  def countPageView(movieId: MovieId, viewTime: DateTime): Future[Unit]
  def getTopTenMoviesSoFarToday(now: DateTime): Future[IndexedSeq[MovieCount]]
}