package name.alex.sp.util

import org.scalatest.{FreeSpecLike, FreeSpec, Matchers}

trait Assertions extends FreeSpecLike with Matchers{
  def collectionsEqual[T](result: Seq[T], expected: Seq[T]) {
    s"have length ${expected.length}" in {
      result should have length expected.length
    }

    for(elt <- expected) {
      s"contain $elt" in {
        result should contain(elt)
      }
    }

    for(
      elt <- result
      if !expected.contains(elt)) {

      s"not contain $elt" in {
        result should not contain(elt)
      }
    }
  }
}
