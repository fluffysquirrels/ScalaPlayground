package name.alex.sp.topTenMovies

import CassandraServiceTestHelper._
import org.scalatest.{BeforeAndAfterEach, BeforeAndAfterAll, Matchers, FreeSpec}
import name.alex.sp.topTenMovies.cassandraImpl.CassandraMoviePageViewsService
import org.joda.time.DateTime
import name.alex.sp.topTenMovies.publicInterface.MovieId

class CassandraServiceTests
    extends FreeSpec
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach {
  val s = new CassandraMoviePageViewsService(clusterBuilder, keyspaceName)
  val testFlag_keep_data = true;

  override protected def beforeAll(): Unit = s.createSchemaIfNotExists()

  override protected def afterAll(): Unit = if(!testFlag_keep_data) { s.dropDataAndSchema() }

  override protected def beforeEach(): Unit = s.truncateData()

  override protected def afterEach(): Unit = super.afterEach()

  "Log a page view" in {
    s.countPageView(MovieId(1), DateTime.now)
  }

}
