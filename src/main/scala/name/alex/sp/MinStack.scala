package name.alex.sp

class MinStack[Elt](implicit eltOrdering: Ordering[Elt]) {
  private abstract class LinkedListNode[T]
  private case class Empty[T]() extends LinkedListNode[T]
  private case class Full[T](val elt: T, val nextNode: LinkedListNode[T]) extends LinkedListNode[T]

  private case class ValueMinPair(val elt: Elt, val currentMin: Elt)

  private[this] var list: LinkedListNode[ValueMinPair] = Empty[ValueMinPair]()

  def push(elt: Elt): Unit = {
    list match {
      case Empty() => {
        list = Full(ValueMinPair(elt, elt), list)
      }
      case Full(ValueMinPair(currentElt, currentMin), nextNode) => {
        val newMin = eltOrdering.min(currentMin, elt)
        list = Full(ValueMinPair(elt, newMin), list)
      }
    }
  }

  def pop(): Option[Elt] = {
    list match {
      case Empty() => None
      case Full(ValueMinPair(elt, _), nextNode) => { list = nextNode; Some(elt) }
    }
  }

  def min: Option[Elt] = {
    list match {
      case Empty() => None
      case Full(ValueMinPair(_, currentMin), _) => { Some(currentMin) }
    }
  }
}