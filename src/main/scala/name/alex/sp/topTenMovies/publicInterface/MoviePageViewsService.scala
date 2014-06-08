package name.alex.sp.topTenMovies.publicInterface

import com.github.nscala_time.time.Imports._

trait MoviePageViewsService {
  def countPageView(movieId: MovieId, viewTime: DateTime): Unit
  def getTopTenMoviesSoFarToday(now: DateTime): IndexedSeq[MovieCount]
}