package name.alex.sp

import org.scalatest.{Matchers, FlatSpec}
import scala.None

class MyPriorityQueueTests extends FlatSpec with Matchers  {
  "Empty PriorityQueue" should "peek returns None" in {
    val q = new MyPriorityQueue[Int]
    q.peek should equal (None)
  }

  it should "have length 0" in {
    val q = new MyPriorityQueue[Int]
    q.length should equal (0)
  }

  it should "throw on dequeue" in {
    val q = new MyPriorityQueue[Int]
    a[NoSuchElementException] should be thrownBy q.dequeue()
  }

  "PriorityQueue containing 1" should "peek returns Some(1)" in {
    val q = new MyPriorityQueue[Int]
    q.enqueue(1)
    q.peek should equal (Some(1))
  }

  it should "have length 1" in {
    val q = new MyPriorityQueue[Int]
    q.enqueue(1)
    q.length should equal (1)
  }

  it should "give 1 on dequeue" in {
    val q = new MyPriorityQueue[Int]
    q.enqueue(1)
    q.dequeue() should equal (1)
  }

  it should "have length 0 after dequeue" in {
    val q = new MyPriorityQueue[Int]
    q.enqueue(1)
    q.dequeue()
    q.length should be (0)
  }

  it should "peek returns None after dequeue" in {
    val q = new MyPriorityQueue[Int]
    q.enqueue(1)
    q.dequeue()
    q.peek should be (None)
  }

  "PriorityQueue enqueued with 1,2,3,4,5" should "dequeue 5,4,3,2,1" in {
    val q = new MyPriorityQueue[Int]
    q.enqueue(1)
    q.enqueue(2)
    q.enqueue(3)
    q.enqueue(4)
    q.enqueue(5)
    q.dequeueAll() should be (Vector(5,4,3,2,1))
  }

  "PriorityQueue enqueued with 4,5,3,2,1" should "dequeue 5,4,3,2,1" in {
    val q = new MyPriorityQueue[Int]
    q.enqueue(4)
    q.enqueue(5)
    q.enqueue(3)
    q.enqueue(2)
    q.enqueue(1)
    q.dequeueAll() should be (Vector(5,4,3,2,1))
  }

  "PriorityQueue enqueued with 1,3,5,2,4" should "dequeue 5,4,3,2,1" in {
    val q = new MyPriorityQueue[Int]
    q.enqueueAll(1,3,5,2,4)
    q.dequeueAll() should be (Vector(5,4,3,2,1))
  }

  "Random PriorityQueue" should "dequeue sorted result" in {
    val randomVals = scala.util.Random.shuffle(Vector(1,2,3,4,5) ++ Vector(1,2,3,4,5))
    println("Shuffled input: ")
    println(randomVals)
    val q = new MyPriorityQueue[Int]
    q.enqueueAll(randomVals : _*)
    q.dequeueAll() should be (Vector(5,5,4,4,3,3,2,2,1,1))
  }
}

