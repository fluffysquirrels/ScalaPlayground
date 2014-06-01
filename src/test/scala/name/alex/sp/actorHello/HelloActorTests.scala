package name.alex.sp.actorHello


import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import org.scalatest.{Matchers, FreeSpec}

sealed trait HelloActorIncomingMessages{}
sealed case class Greeting(fromName: String) extends HelloActorIncomingMessages

class HelloActor(val myName: String) extends Actor {

  def receive = {
      case Greeting(fromName) => println(s"A pleasure to meet you, $fromName. I am $myName.")
      case _ => println("huh?")
    }
}

class HelloTests extends FreeSpec with Matchers {
  "Do some stuff" in {
    val system = ActorSystem("HelloSystem")
    val helloActor = system.actorOf(Props(new HelloActor("Plato")), name = "helloActor")
    helloActor ! new Greeting("HelloTests")
    helloActor ! 42
  }
}
