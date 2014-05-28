package name.alex.sp

import org.scalatest.{FreeSpec, Matchers}
import scala.collection.mutable

/*
  From Cracking the coding interview 3.2:
  How would you design a stack which, in addition to push and pop, also has a function
  min which returns the minimum element? Push, pop and min should all operate in
  O(1) time.
 */

class MinStackTests extends FreeSpec with Matchers {
  "With an empty MinStack" - {
    "pop() returns None" in {
      val it = new MinStack[Int]
      it.pop() should equal(None)
    }

    "min returns None" in {
      val it = new MinStack[Int]
      it.min should equal(None)
    }

    "push(1), then pop() returns Some(1)" in {
      val it = new MinStack[Int]
      it.push(1)
      it.pop() should equal(Some(1))
    }
  }

  "With a MinStack containing 1,2,3" - {
    "pop() returns 3,2,1" in {
      val it = new MinStack[Int]
      it.push(1)
      it.push(2)
      it.push(3)
      it.pop() should equal(Some(3))
      it.pop() should equal(Some(2))
      it.pop() should equal(Some(1))
    }

    "min returns 1" in {
      val it = new MinStack[Int]
      it.push(1)
      it.push(2)
      it.push(3)
      it.min should equal(Some(1))
    }
  }

  "With a MinStack containing 3,2,1" - {
    "pop() returns 1,2,3" in {
      val it = new MinStack[Int]
      it.push(3)
      it.push(2)
      it.push(1)
      it.pop() should equal(Some(1))
      it.pop() should equal(Some(2))
      it.pop() should equal(Some(3))
    }

    "min returns 1" in {
      val it = new MinStack[Int]
      it.push(3)
      it.push(2)
      it.push(1)
      it.min should equal(Some(1))
    }
  }
}