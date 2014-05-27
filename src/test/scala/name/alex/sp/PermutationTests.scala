package name.alex.sp

import org.scalatest.{FreeSpec, Matchers}


class PermutationTests extends FreeSpec with Matchers with name.alex.sp.util.Assertions {

  def getEm(elts: Vector[Int]): Seq[Seq[Int]] = {
    if(elts.isEmpty) {
      Vector()
    }
    if(elts.length == 1){
      return Vector(elts)
    }
    for(
      headIx <- 0 until elts.length;
      head = elts(headIx);
      rest = elts.patch(headIx, Vector(), 1);
      subPerm <- getEm(rest)
    ) yield Vector(head) ++ subPerm
  }

  "1,2,3" - {
    val it = Vector(1,2,3)

    "its permutations should" - {
      val perms = getEm(it)

      collectionsEqual(perms,
        Vector(
          Vector(1, 2, 3),
          Vector(1, 3, 2),
          Vector(2, 1, 3),
          Vector(2, 3, 1),
          Vector(3, 1, 2),
          Vector(3, 2, 1)
        )
      )
    }
  }
}
