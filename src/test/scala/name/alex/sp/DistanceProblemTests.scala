package name.alex.sp

import org.scalatest.{FreeSpec, Matchers}

/*
  From http://www.careercup.com/question?id=5664366808530944

  You are given a 2D grid in which each cell is either empty, contains an entry “D” which stands for Door, or an entry
  “W” which stands for wall (Obstacle). You can move in any of the four directions from each empty position in the grid.
  Of course you cannot move into a cell that has “W” in it. You need to fill each empty cell with a number that
  represents the distance of the closest door to that cell.
*/
class DistanceProblemTests extends FreeSpec with Matchers {
  val playMap: Array[String] =
    """.....
      |.WDW.
      |.WWW.
      |.....
      |WWWWW
      |D....
      |.....
      |WWWWW
      |.....""".stripMargin.split("\n")

  private sealed abstract class VisitState
  private case object NotVisited extends VisitState
  private case object Visiting extends VisitState
  private case object Visited extends VisitState

  "Do that thing" in {
    val map: Array[String] = playMap
    val width = map(0).length
    val height = map.length
    val doors =
      for(
        y <- 0 until height;
        x <- 0 until width;
        if map(y)(x) == 'D'
      ) yield (y,x)

    val distances = Array.tabulate(height,width) { (y,x) => if(doors.contains((y,x))) 0 else Int.MaxValue }

    computeDistances()

    showDistances(distances)

    def computeDistances() {
      val dfsVisit: Array[Array[VisitState]] = Array.fill(height, width) { NotVisited }

      for((y,x) <- doors) {
        dfs(y,x)
      }

      def dfs(y: Int, x: Int) {
        dfsVisit(y)(x) match {
          case Visited => {}
          case Visiting => {}
          case NotVisited => {
            dfsVisit(y).update(x, Visiting)

            val neighbours = getEmptyNeighbours(y,x)
            val myDistance = distances(y)(x)
            for((ny,nx) <- neighbours
                if distances(ny)(nx) > myDistance + 1){
              distances(ny).update(nx, myDistance + 1)
              dfsVisit(ny).update(nx, NotVisited)
              dfs(ny, nx)
            }

            dfsVisit(y).update(x, Visited)
          }
        }
      }

      def getEmptyNeighbours(y: Int, x: Int): Seq[(Int,Int)] = {
        getNeighbours(y,x).filter {
          case (y,x) => map(y)(x) == '.'
        }
      }

      def getNeighbours(y: Int, x: Int): Seq[(Int,Int)] = {
        val possNeighbours =
          Vector(
            (y+1, x  ),
            (y-1, x  ),
            (y,   x+1),
            (y,   x-1)
          )
        possNeighbours.filter {
          case (y,x)
          => (y < height
             && y >= 0
             && x < width
             && x >= 0)}
      }
    }

    def showDistances(distances: Array[Array[Int]]) {
      for(row <- distances){
        println(row.mkString(" "))
      }
    }
  }
}
