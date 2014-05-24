package name.alex.sp

import org.scalatest.{FlatSpec, Matchers}
import scala.collection.mutable

class ManyWayMerge extends FlatSpec with Matchers{
  mergeExampleTestWithPriorityQueue((Vector()), (Vector()))
  mergeExampleTestWithPriorityQueue(Vector(Vector(1)), Vector(1))
  mergeExampleTestWithPriorityQueue(Vector(Vector(), Vector(1), Vector()), Vector(1))
  mergeExampleTestWithPriorityQueue(Vector(Vector(2), Vector(1)), Vector(1, 2))
  mergeExampleTestWithPriorityQueue(Vector(Vector(1,3,5), Vector(2,4,6)), Vector(1,2,3,4,5,6))
  mergeExampleTestWithPriorityQueue(Vector(Vector(3,6,9), Vector(2,5,8), Vector(1,4,7)), Vector(1,2,3,4,5,6,7,8,9))

  class ListElement[A]
    (val value: A, val rest: Iterable[A])
    (implicit ev: Numeric[A])
    extends Ordered[ListElement[A]] {

    override def compare(that: ListElement[A]): Int = {
      return -ev.compare(this.value, that.value)
    }
  }

  def manyWayMergeWithPriorityQueue[A]
    (in: Iterable[Iterable[A]])
    (implicit ev: Numeric[A])
    : scala.collection.immutable.IndexedSeq[A] = {

    val q = new mutable.PriorityQueue[ListElement[A]]()

    for(seq <- in if seq.nonEmpty){
      q.enqueue(new ListElement(seq.head, seq.tail))
    }

    var rv = Vector[A]()

    while(q.nonEmpty){
      val elt = q.dequeue()
      rv = rv :+ elt.value
      if(elt.rest.nonEmpty) {
        q.enqueue(new ListElement(elt.rest.head, elt.rest.tail))
      }
    }

    return rv
  }

  def mergeExampleTestWithPriorityQueue
    (input: Iterable[Iterable[Int]], expectedOutput: Iterable[Int]){
    input.toString should "merge to " + expectedOutput.toString in {
      val merged = manyWayMergeWithPriorityQueue(input)
      merged should equal (expectedOutput)
    }
  }
}
