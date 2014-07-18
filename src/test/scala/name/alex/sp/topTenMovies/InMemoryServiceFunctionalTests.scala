package name.alex.sp.topTenMovies

import name.alex.sp.topTenMovies.inMemoryImpl.InMemoryMoviePageViewsService
import name.alex.sp.topTenMovies.publicInterface.MoviePageViewsService

class InMemoryServiceFunctionalTests
    extends AbstractTopTenMoviesFunctionalTests {
  override def getService(): MoviePageViewsService =
    new InMemoryMoviePageViewsService
}
