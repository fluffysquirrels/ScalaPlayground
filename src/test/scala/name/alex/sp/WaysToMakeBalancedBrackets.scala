package name.alex.sp

import org.scalatest.{FreeSpec, Matchers}

/*
  From Cracking the coding interview 8.5:
  Implement an algorithm to print all valid (e.g., properly opened and closed) combi-
  nations of n-pairs of parentheses.
  EXAMPLE:
  input: 3 (e.g., 3 pairs of parentheses)
  output: ()()(), ()(()), (())(), ((()))
 */
class WaysToMakeBalancedBrackets extends FreeSpec with Matchers{



  "Balanced brackets" - {
    example(1, 1)
    example(2, 2)
    example(3, 5)
    example(4, 14)

    def example(numPairs: Int, expectedNumWays: Int) {
      s"Should be $expectedNumWays way(s) to make $numPairs pair(s) of brackets" in {
        val numWays = getNumWays(numPairs)
        numWays should equal(expectedNumWays)
      }
    }

//    def getNumWays(numPairs:Int): Int = {
//      if(numPairs == 1) {
//        return 1
//      }
//
//      val numLeftRightSplitWays = (for(
//        numPairsInLeftPart <- 1 until numPairs;
//        numWaysInLeftPart = getNumWays(numPairsInLeftPart - 1); // Outermost on left chunk is a wrapping, not more
//                                                                // left-right splits to prevent double counting
//                                                                // like ().()() and ()().() where . shows a left-right
//                                                                // split.
//        numPairsInRightPart = numPairs - numPairsInLeftPart;
//        numWaysInRightPart = getNumWays(numPairsInRightPart)
//      ) yield numWaysInLeftPart * numWaysInRightPart).sum
//      val numWrappedWays = getNumWays(numPairs - 1)
//
//      return numLeftRightSplitWays + numWrappedWays
//    }

    def getNumWays(numPairs: Int): Int = {
      val ways = getWays(numPairs)
      println(s"Found ${ways.length} way(s):")
      for (way <- ways) {
        println(s"    $way")
      }
      println()

      ways.length
    }

    def getWays(numPairs: Int): Seq[String] = {
      if(numPairs == 1) {
        return Vector("()")
      }

      if(numPairs == 0) {
        return Vector("")
      }

      if(numPairs < 0) {
        sys.error(s"Cannot find ways when numPairs is < 0; it is $numPairs")
      }

      for(numPairsInLeftPart <- 1 to numPairs;
          numPairsInRightPart = numPairs - numPairsInLeftPart;
          leftWays = getWays(numPairsInLeftPart - 1);
          rightWays = getWays(numPairsInRightPart);
          leftWay <- leftWays;
          rightWay <- rightWays
      ) yield s"($leftWay)$rightWay"
    }
  }
}