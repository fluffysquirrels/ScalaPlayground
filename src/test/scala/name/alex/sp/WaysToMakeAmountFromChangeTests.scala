package name.alex.sp

import org.scalatest.{FreeSpec, Matchers}
import scala.collection.immutable.HashMap
import scala.util.Sorting

/*
From Cracking the coding interview 8.7:
Given an infinite number of quarters (25 cents), dimes (10 cents), nickels (5 cents) and
pennies (1 cent), write code to calculate the number of ways of representing n cents.
 */
class WaysToMakeAmountFromChangeTests extends FreeSpec with Matchers {
  "With 25 cents, 10 cents, 5 cents, 1 cent" - {

    example(0, 1)
    example(1, 1)
    example(2, 1)
    example(3, 1)
    example(4, 1)
    example(5, 2)
    example(6, 2)
    example(7, 2)
    example(8, 2)
    example(9, 2)
    example(10, 4)
    example(212, 1670)

    def example(target: Int, expectedWays: Int): Unit = {
      s"should be $expectedWays ways to make ${target}¢" in {
        val theWays = getWays(target, coinAmounts)
        println(s"Found ${theWays.length} way(s) to make $target")
        for(way <- theWays) {
          val mySeq: Seq[(Int, Int)] = way.iterator.toSeq
          val coinString =
            Sorting.stableSort(mySeq)
              .map (kv => s"${kv._1} cent(s) × ${kv._2}")
              .mkString(", ")
          println(s"  $coinString")
        }
        println()

        val numWays = theWays.length
        numWays should equal(expectedWays)
      }
    }
  }

  val coinAmounts = Vector(25,10,5,1)

  def getWays(targetAmount: Int, coinAmounts: Vector[Int]): Seq[Map[Int, Int]] = {
    if(targetAmount < 0) {
      sys.error(s"Cannot call getWays with negative targetAmount $targetAmount")
    }

    if(targetAmount == 0) {
      return Seq(Map())
    }

    val smallEnoughCoins = coinAmounts.filter { c => c <= targetAmount }

    for(
      thisCoin <- smallEnoughCoins;
      remainingAmount = targetAmount - thisCoin;
      // The set remainingAmountCoins is monotonic decreasing to prevent
      // double counting 1 + 5 and 5 + 1
      remainingAmountCoins = coinAmounts.filter {c => c <= thisCoin};
      remainingAmountWays = getWays(remainingAmount, remainingAmountCoins);
      remainingAmountWay <- remainingAmountWays
    ) yield remainingAmountWay.updated(thisCoin, 1 + remainingAmountWay.getOrElse(thisCoin, 0))
  }


}
