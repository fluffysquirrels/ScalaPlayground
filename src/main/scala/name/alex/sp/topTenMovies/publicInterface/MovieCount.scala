package name.alex.sp.topTenMovies.publicInterface

sealed case class MovieCount(movieId: MovieId, pageViewsInPeriod: PageViewsCount)