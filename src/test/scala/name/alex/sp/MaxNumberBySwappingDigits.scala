package name.alex.sp

import scala.math.Ordering.Implicits._
import org.scalatest.{Matchers, FlatSpec}

// From http://www.careercup.com/question?id=5638261762424832 :
// Given a array int a[]={2,5,1,9,3,7,2,8,9,3} and the no. of swap operations.We are allowed to do swap operations.
// swap constraint: exchange only adjacent element.
// Find the max number that can be formed using swap operations.
//
// public static  int[] maximize(int arr[],int swapsAllowed);

class MaxNumberBySwappingDigits extends FlatSpec with Matchers {

  maxSwapsExample(Vector(2,3,5), 0, Vector(2,3,5))
  maxSwapsExample(Vector(2,3,5), 1, Vector(3,2,5))
  maxSwapsExample(Vector(2,3,5), 2, Vector(5,2,3))
  maxSwapsExample(Vector(2,3,5), 3, Vector(5,3,2))

  val bigExample = Vector(2,5,1,9,3,7,2,8,9,3)

  for(maxSwaps <- 0 to 10) {
    printResultExample(bigExample, maxSwaps)
  }

  def printResultExample(in: Vector[Int], maxNumSwaps: Int) {
    in.mkString(", ") should "getMax with " + maxNumSwaps + " should not die horribly in a fire when we run " in {
      val max = getMaxWithSwapsSlow(in, maxNumSwaps)
      println(max)
    }
  }

  def maxSwapsExample(in: Vector[Int], maxNumSwaps: Int, expected: Vector[Int]) {
    in.mkString(", ") should "have max " + expected.mkString(",") + " with " + maxNumSwaps + " swap(s)" in {
      getMaxWithSwapsSlow(in, maxNumSwaps) should be (expected)
    }
  }

  def getMaxWithSwapsSlow(xs: Vector[Int], maxNumSwaps: Int): Vector[Int] = {
    if (maxNumSwaps == 0) {
      return xs
    }

    val nSwaps = getUpToNSwaps(xs, maxNumSwaps)
    return nSwaps.max
  }

  def getUpToNSwaps(xs: Vector[Int], n: Int): Seq[Vector[Int]] = {
    if (n == 0) {
      List(xs)
    }
    else {
      val nMinusOneSwaps = getUpToNSwaps(xs, n-1)
      val nSwaps = for (nMinusOneSwap <- nMinusOneSwaps;
                        nSwap <- getOneSwaps(nMinusOneSwap))
        yield nSwap
      return (nMinusOneSwaps ++ nSwaps).distinct
    }
  }

  def getOneSwaps(xs: Vector[Int]): Seq[Vector[Int]] =
    return for (i: Int <- 0 to xs.length - 2)
    yield xs.patch(i, List(xs(i + 1), xs(i)), 2)
}

