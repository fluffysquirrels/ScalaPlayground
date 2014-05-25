package name.alex.sp.coffeeShop

import java.util.concurrent.{TimeUnit, Executors}
import scala.concurrent._

import name.alex.sp.FunctionalUtils
import FunctionalUtils._

class Shop {

  private[this] val executor = Executors.newScheduledThreadPool(/* corePoolSize */ 0)

  def order(request: OrderRequest): OrderProgressCustomer = {
    val progress = new OrderProgressStaff(request)
    beginFulfilment(progress)

    progress.forCustomer
  }

  private[this] def beginFulfilment(progress: OrderProgressStaff) = {
    executor.schedule(toRunnable (() => progress.allFinishedItems.success(Seq(CoffeeItem()))), 200, TimeUnit.MILLISECONDS)
  }
}

