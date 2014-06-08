package name.alex.sp.util

import java.util.concurrent.atomic.AtomicLong
import scala.collection.concurrent.TrieMap
import scala.collection.mutable

class ConcurrentCountMap[K] private(counts: scala.collection.concurrent.TrieMap[K, AtomicLong])
    extends scala.collection.MapLike[K, Long, ConcurrentCountMap[K]]
    with scala.collection.Map[K, Long] {

  def this() = this(new TrieMap[K, AtomicLong]())

  /**
   * Updates the count for a key
   *
   * @param k           key for the count to be updated
   * @param countDelta  how much to modify the count of this key by
   * @return            the new value of the count of this key
   */
  def add(k: K, countDelta: Long): Long = {
    // I would prefer to use counts.getOrElseUpdate, but this is not thread-safe in Scala v2.10.x
    // (see https://issues.scala-lang.org/browse/SI-7943).

    var counter: Option[AtomicLong] = None

    // Loop if the counter we want is absent until we get back the new counter that we put in.
    // Another thread could be concurrently removing the counter we add, so we need the loop.
    while(counter == None){
      counts.putIfAbsent(k, new AtomicLong(0))
      counter = counts.get(k)
    }

    counter.get.addAndGet(countDelta)
  }

  /**
   * Get the count for a key
   *
   * @param k key for the count to retrieve
   * @return Some(value) if a count was found for the key, or None if a count was not found
   */
  def get(k: K): Option[Long] = {
    val optionCounter = counts.get(k)
    optionAtomicLongToLong(optionCounter)
  }

  def remove(k: K): Option[Long] = {
    val optionCounter = counts.remove(k)
    optionAtomicLongToLong(optionCounter)
  }

  def clear(): Unit = {
    counts.clear()
  }

  override def iterator: Iterator[(K, Long)] = {
    counts.iterator.map {case (k, atomicLong) => (k, atomicLong.get())}
  }

  private def optionAtomicLongToLong(atomicLong: Option[AtomicLong]): Option[Long] = atomicLong match {
    case Some(atomicLong) => Some(atomicLong.get())
    case None => None
  }

  override def empty: ConcurrentCountMap[K] = new ConcurrentCountMap[K]()

  override def seq: collection.Map[K, Long] = this

  override def +[B1 >: Long](kv: (K, B1)): collection.Map[K, B1] = kv match {
    case(k, v: Long) => {
      val newCounts = counts.clone()
      newCounts += ((k, new AtomicLong(v)))
      new ConcurrentCountMap[K](newCounts)
    }
  }

  override def -(key: K): ConcurrentCountMap[K] = {
    val newCounts = counts.clone()
    newCounts -= key
    new ConcurrentCountMap[K](newCounts)
  }

  override def newBuilder: mutable.Builder[(K, Long), ConcurrentCountMap[K]] =
    new mutable.ArrayBuffer[(K, Long)].mapResult( ab => {
      val rv = new ConcurrentCountMap[K]()
      for((k,v)<- ab){
        rv.add(k, v)
      }
      rv
    })
}
