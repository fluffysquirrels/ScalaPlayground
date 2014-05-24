/**
 * Created by alex on 18/05/14.
 */
object Main {

  def testCaseClasses: Unit = {
    println("x")
  }

  def main(args: Array[String]) {
    println("cats");
    testCaseClasses
  }
}

abstract class Person{}
case class Fred(var pintsDrunk: Int) extends Person
case class George(fightStartedWith: String) extends Person