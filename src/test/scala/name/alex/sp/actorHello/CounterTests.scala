package name.alex.sp.actorHello

import org.scalatest.{Matchers, FreeSpec}
import akka.actor.{ActorRef, Actor, Props, ActorSystem}
import akka.pattern.ask
import scala.concurrent.{Promise, Future, ExecutionContext, Await}
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import akka.util.Timeout
import java.lang.Runnable
import name.alex.sp.FunctionalUtils._
import name.alex.sp.ConsoleLogger

class CounterTests extends FreeSpec with Matchers {
  "Increment counter" in {
    val sys = ActorSystem("mas")
    val counter = sys.actorOf(name = "myCounter", props = Props(new CounterActor()))

    implicit val timeout = Timeout(Duration(10, TimeUnit.MILLISECONDS))

    counter ! Add(1)
    val counterF = counter ? Get
    Await.result(counterF, timeout.duration) shouldEqual 1
  }

  val log = new ConsoleLogger("CounterTests")

  "One counter spammer" in {
    val sys = ActorSystem("mas")
    val counter = sys.actorOf(name = "myCounter", props = Props(new CounterActor()))

    implicit val timeout = Timeout(Duration(10000, TimeUnit.MILLISECONDS))

    val numCounts: Int = 100000

    val spammerF = beginCounterSpammer(counter, sys.dispatcher, numCounts, "1")

    Await.ready(spammerF, timeout.duration)
    log.log("Begin ask for count")
    val counterF = counter ? Get
    val count = Await.result(counterF, timeout.duration)
    log.log("Done ask for count")
    count shouldEqual numCounts
  }

  "N counter spammers" in {
    val sys = ActorSystem("mas")
    val counter = sys.actorOf(name = "myCounter", props = Props(new CounterActor()))

    implicit val timeout = Timeout(Duration(10000, TimeUnit.MILLISECONDS))
    implicit val ec = sys.dispatcher

    val numCountsPerSpammer: Int = 1000000
    val numSpammers: Int = 5

    val spammerFutures =
      for(spammerNum <- 1 to numSpammers)
        yield beginCounterSpammer(counter, sys.dispatcher, numCountsPerSpammer, spammerNum.toString)

    Await.ready(Future.sequence(spammerFutures), timeout.duration)
    log.log("Begin ask for count")
    val counterF = counter ? Get
    val count = Await.result(counterF, timeout.duration)
    log.log("Done ask for count")
    count shouldEqual (numCountsPerSpammer * numSpammers)
  }

  def beginCounterSpammer(counterRef: ActorRef, executor: ExecutionContext, numCounts: Int, spammerName: String): Future[Boolean] = {
    val p = Promise[Boolean]()
    val log = new ConsoleLogger(s"Spammer $spammerName")
    log.log("Begin beginCounterSpammer")
    executor.execute(toRunnable (() => {
      log.log("Begin counter spammer main")

      for(i <- 1 to numCounts){
        counterRef ! Add(1)
      }

      log.log("Done counter spammer main")
      p.success(true)
    }))

    log.log("Done beginCounterSpammer")
    p.future
  }
}

sealed trait CounterActorIncoming
sealed case class Add(num: Int) extends CounterActorIncoming
object Get extends CounterActorIncoming

class CounterActor extends Actor {
  var _count: Int = 0
  def count = _count

  override def receive: Receive = {
    case Add(num) => _count += num
    case Get => sender ! _count
  }
}