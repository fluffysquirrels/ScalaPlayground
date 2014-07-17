package name.alex.sp.topTenMovies

import org.scalatest.{Matchers, FreeSpec}
import name.alex.sp.topTenMovies.inMemoryImpl.InMemoryMoviePageViewsService
import org.joda.time.DateTime
import name.alex.sp.topTenMovies.publicInterface._
import name.alex.sp.util.Assertions

class InMemoryServiceFunctionalTests extends FreeSpec with Matchers with Assertions{
  private val anyMovieId = MovieId(0)
  private val firstMovieId = MovieId(1)
  private val secondMovieId = MovieId(2)

  "When no movies have a page view, the top 10 list is empty" in {
    val service = getService()
    val now = DateTime.now

    service.getTopTenMoviesSoFarToday(now) shouldEqual Vector()
  }

  "When one movie has one page view, that movie is the only answer in the top 10 list, with one page view" in {
    val service = getService()
    val now = DateTime.now

    service.countPageView(anyMovieId, now)
    service.getTopTenMoviesSoFarToday(now) shouldEqual Vector(MovieCount(anyMovieId, PageViewsCount(1)))
  }

  "When one movie has two page views, that movie is the only answer in the top 10 list, with two page views" in {
    val service = getService()
    val now = DateTime.now

    service.countPageView(anyMovieId, now)
    service.countPageView(anyMovieId, now)
    service.getTopTenMoviesSoFarToday(now) shouldEqual Vector(MovieCount(anyMovieId, PageViewsCount(2)))
  }

  "When two movies have one page view each, the top 10 list is those two movies in some order" in {
    val service = getService()
    val now = DateTime.now

    service.countPageView(firstMovieId, now)
    service.countPageView(secondMovieId, now)
    service.getTopTenMoviesSoFarToday(now) should contain theSameElementsAs
      Vector(MovieCount(firstMovieId, PageViewsCount(1)),
             MovieCount(secondMovieId, PageViewsCount(1)))
  }

  "When one movie has two page views and another has one page view, the top 10 list is those two movies with the most viewed movie first" in {
    val service = getService()
    val now = DateTime.now

    service.countPageView(firstMovieId, now)
    service.countPageView(secondMovieId, now)
    service.countPageView(firstMovieId, now)
    service.getTopTenMoviesSoFarToday(now) should contain theSameElementsInOrderAs
      Vector(MovieCount(firstMovieId, PageViewsCount(2)),
             MovieCount(secondMovieId, PageViewsCount(1)))
  }

  "When 10 popular movies have two page views each and 10 unpopular movies have one page view each," +
    "the top 10 list is the 10 popular movies" in {
    val service = getService()
    val now = DateTime.now

    val popularMovieIds = (1 to 10).map {n => MovieId(n)}
    val unpopularMovieIds = (101 to 110).map {n => MovieId(n)}

    for(id <- popularMovieIds) {
      service.countPageView(id, now)
      service.countPageView(id, now)
    }

    for(id <- unpopularMovieIds) {
      service.countPageView(id, now)
    }

    val expectedTop10 = popularMovieIds.map {id => MovieCount(id, PageViewsCount(2))}

    service.getTopTenMoviesSoFarToday(now) should contain theSameElementsAs expectedTop10
  }

  def getService(): MoviePageViewsService = {
    new InMemoryMoviePageViewsService()
  }
}
