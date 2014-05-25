package name.alex.sp.coffeeShop

import scala.concurrent.{Promise, Future}

class OrderProgressStaff(val request: OrderRequest) {
  val allFinishedItems: Promise[Seq[Item]] = Promise()
  val forCustomer = new OrderProgressCustomer(request, allFinishedItems.future)
}

class OrderProgressCustomer(val request: OrderRequest, val allFinishedItems: Future[Seq[Item]]) {

}
