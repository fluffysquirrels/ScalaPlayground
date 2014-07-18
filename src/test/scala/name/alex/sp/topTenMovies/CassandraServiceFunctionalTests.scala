package name.alex.sp.topTenMovies

import CassandraServiceTestHelper._
import org.scalatest.{BeforeAndAfterEach, BeforeAndAfterAll, Matchers, FreeSpec}
import name.alex.sp.topTenMovies.cassandraImpl.CassandraMoviePageViewsService
import org.joda.time.DateTime
import name.alex.sp.topTenMovies.publicInterface.{MoviePageViewsService, MovieId}

class CassandraServiceFunctionalTests
    extends AbstractTopTenMoviesFunctionalTests
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach {
  val s = new CassandraMoviePageViewsService(clusterBuilder, keyspaceName)

  override protected def beforeAll(): Unit = s.createSchemaIfNotExists()

  override protected def afterAll(): Unit = s.dropDataAndSchema()

  override protected def beforeEach(): Unit = s.truncateData()

  override protected def afterEach(): Unit = super.afterEach()

  override def getService(): MoviePageViewsService = s
}
