package name.alex.sp

import scala.collection.mutable

/**
 * Created by alex on 23/05/14.
 */
/*
  A collection of values that allows fast access to -- O(1) -- and removal
  of -- O(log n) -- the largest element in the collection.
 */
class MyPriorityQueue[Elt](implicit ev: Ordering[Elt]) {
  private val buffer = mutable.Buffer[Elt]()

  def enqueue(elt: Elt) {
    buffer += elt
    pushUp(buffer.length - 1)
  }

  def enqueueAll(elts: Elt*) {
    for(e <- elts) {
      enqueue(e)
    }
  }

  def dequeue(): Elt = {
    if(buffer.length == 0) {
      throw new NoSuchElementException("Cannot dequeue from empty queue")
    }

    val rv = buffer(0)

    if(buffer.length == 1) {
      buffer.clear()
    }
    else {
      val wasLast = buffer.last
      buffer.trimEnd(1)
      buffer.update(0, wasLast)
      pushDown(0)
    }

    return rv
  }

  def dequeueAll(): IndexedSeq[Elt] = {
    var retBuilder = Vector.newBuilder[Elt]

    while(nonEmpty) {
      retBuilder += dequeue()
    }

    return retBuilder.result()
  }

  def peek: Option[Elt] =
    if (buffer.isEmpty) Option.empty
    else Option(buffer(0))

  def length: Int = buffer.length

  def isEmpty: Boolean = length == 0
  def nonEmpty: Boolean = length > 0

  private def pushDown(idx: Int) {
    val currVal = buffer(idx)

    val leftChildIdx = getLeftChildIndex(idx)
    if(leftChildIdx >= buffer.length) return
    val leftChildVal = buffer(leftChildIdx)

    val rightChildIdx = getRightChildIndex(idx)

    val biggestChildIdx =
      if(rightChildIdx >= buffer.length)
        leftChildIdx
      else {
        if (ev.gt(leftChildVal, buffer(rightChildIdx)))
          leftChildIdx
        else
          rightChildIdx
      }
    val biggestChildVal = buffer(biggestChildIdx)

    if(ev.gt(biggestChildVal, currVal)) {
      buffer.update(idx, biggestChildVal)
      buffer.update(biggestChildIdx, currVal)
      pushDown(biggestChildIdx)
      return
    }
  }

  private def pushUp(idx: Int) {
    if(idx == 0) return

    val currVal = buffer(idx)

    val parentIdx = getParentIndex(idx)
    val parentVal = buffer(parentIdx)

    if(ev.gt(currVal, parentVal)) {
      buffer.update(parentIdx, currVal)
      buffer.update(idx, parentVal)
      pushUp(parentIdx)
      return
    }
  }

  private def getLeftChildIndex(idx: Int) = idx * 2 + 1
  private def getRightChildIndex(idx: Int) = idx * 2 + 2
  private def getParentIndex(idx: Int) = (idx - 1) / 2
}
