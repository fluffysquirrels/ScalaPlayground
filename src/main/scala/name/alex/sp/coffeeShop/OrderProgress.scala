package name.alex.sp.coffeeShop

import scala.concurrent.{Promise, Future}
import java.util.concurrent.atomic.AtomicInteger
import name.alex.sp.util.Counter

class OrderProgressStaff(val request: OrderRequest) {
  val number = OrderProgressStaff.orderCounter.getNextValue()
  val allFinishedItems: Promise[Seq[Item]] = Promise()
  val forCustomer = new OrderProgressCustomer(this)
}

object OrderProgressStaff {
  private val orderCounter = new Counter()
}

class OrderProgressCustomer(val orderProgressStaff: OrderProgressStaff) {
  def allFinishedItems: Future[Seq[Item]] = orderProgressStaff.allFinishedItems.future
  def request = orderProgressStaff.request
}
