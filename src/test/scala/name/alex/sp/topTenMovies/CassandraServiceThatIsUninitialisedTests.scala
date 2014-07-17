package name.alex.sp.topTenMovies

import org.scalatest.{Matchers, FreeSpec}
import CassandraServiceTestHelper._

class CassandraServiceThatIsUninitialisedTests extends FreeSpec with Matchers {
  "Create and destroy schema" in {
    withTestCassandraService(s => {
      s.createSchemaIfNotExists()
      s.dropDataAndSchema()
    })
  }
}
