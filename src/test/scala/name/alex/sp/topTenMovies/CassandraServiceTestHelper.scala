package name.alex.sp.topTenMovies

import name.alex.sp.topTenMovies.cassandraImpl.CassandraMoviePageViewsService
import resource._
import com.datastax.driver.core.Cluster

object CassandraServiceTestHelper {
  def withTestCassandraService(body: (CassandraMoviePageViewsService) => Unit): Unit = {
    for(s <- managed(new CassandraMoviePageViewsService(clusterBuilder, keyspaceName))) {
      body(s)
    }
  }

  val keyspaceName = "movie_page_views_test"
  val clusterBuilder =
    Cluster.builder()
      .addContactPoints("127.0.0.1", "127.0.0.2", "127.0.0.3")
      .withPort(9042)
}
