package name.alex.sp.topTenMovies.cassandraImpl

import com.datastax.driver.core.{CloseFuture, Cluster}
import java.util.concurrent.{ScheduledExecutorService, Executors}
import java.util.UUID
import resource.managed
import scala.collection.JavaConversions._
import name.alex.sp.util.ConsoleLogger
import name.alex.sp.topTenMovies.publicInterface.{MovieId, MovieCount, MoviePageViewsService}
import com.github.nscala_time.time.Imports.DateTime
import org.joda.time
import scala.util.Random
import com.datastax.driver.core.utils.UUIDs

class CassandraMoviePageViewsService(clusterBuilder: Cluster.Builder,
                                     val keyspaceName: String)
    extends MoviePageViewsService {

  // TODO: Use connection pooling instead of connecting to the cluster each time we do something.

  val rawCountsTableName = "movie_raw_page_views"
  val collatedCountsTableName = "movie_collated_page_views"
  val numShards = 2;

  val logger = new ConsoleLogger("CassandraMoviePageViewsService")

  val random = new Random()

  val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(0)

  val cluster = {
    logger.log("> clusterBuilder.build() ")
    val rv = clusterBuilder
      .build()
    logger.log("< clusterBuilder.build() ")

    rv
  }

  def createSchemaIfNotExists(): Unit = {
    {
      logger.log("> createSchemaIfNotExists")

      for (session <- managed(cluster.connect())) {

        val rsKeyspace = session.execute(
          s"""
         |CREATE KEYSPACE IF NOT EXISTS $keyspaceName
         |WITH replication = {
         |  'class': 'SimpleStrategy',
         |  'replication_factor': 2
         |};
      """.stripMargin)

        val rsTable = session.execute(
          s"""
          |CREATE TABLE IF NOT EXISTS $keyspaceName.$rawCountsTableName (
          |  shard_id int,
          |  movie_id bigint,
          |  period_start timestamp,
          |  count_id timeuuid,
          |
          |  PRIMARY KEY ((shard_id, movie_id, period_start), count_id)
          |);
        """.stripMargin
        )
      }
    }

    logger.log("< createSchemaIfNotExists")
  }

  def dropDataAndSchema(): Unit = {
    logger.log("> dropDataAndSchema")

    // NB: Triggers automatic snapshot to keep the data.
    for(session <- managed(cluster.connect())){
      session.execute(
        s"""
         | DROP TABLE $keyspaceName.$rawCountsTableName;
         |
         """.stripMargin)
      session.execute(
        s"""
         | DROP KEYSPACE $keyspaceName;
        """.stripMargin)
    }

    logger.log("< dropDataAndSchema")
  }

  def truncateData(): Unit = {
    logger.log("> truncateData")

    for(session <- managed(cluster.connect())) {
      session.execute(
        s"""
         | TRUNCATE $keyspaceName.$rawCountsTableName;
         |
         """.stripMargin)
    }

    logger.log("< truncateData")
  }

  override def close(): Unit = {
    logger.log("> close ")
    cluster.close()
    logger.log("< close ")
  }

  override def countPageView(movieId: MovieId, viewTime: DateTime): Unit = {
    logger.log("> countPageView ")

//    |  shard_id int,
//    |  movie_id bigint,
//    |  time_block bigint,
//    |  count_id timeuuid,

    val shardId = random.nextInt(numShards)
    val periodStartMillisSinceEpoch = getViewTimePeriodStart(viewTime).getMillis
    val countId = UUIDs.timeBased()

    // TODO: Try using prepared statements.
    for(session <- managed(cluster.connect())){
      session.execute(
        s"""
           |  INSERT INTO $keyspaceName.$rawCountsTableName(
           |    shard_id,
           |    movie_id,
           |    period_start,
           |    count_id
           |  )
           |  VALUES(?, ?, ?, ?)
         """.stripMargin,
        shardId: java.lang.Integer,
        movieId.id: java.lang.Long,
        periodStartMillisSinceEpoch: java.lang.Long,
        countId
      )
    }

    logger.log("< countPageView ")
  }

  override def getTopTenMoviesSoFarToday(now: DateTime): IndexedSeq[MovieCount] = ???


  private def getViewTimePeriodStart(viewTime: DateTime): DateTime = {
    viewTime.withZone(time.DateTimeZone.UTC).withTimeAtStartOfDay()
  }
}
