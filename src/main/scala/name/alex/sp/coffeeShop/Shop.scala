package name.alex.sp.coffeeShop

import java.util.concurrent.{ScheduledExecutorService, TimeUnit, Executors}
import scala.concurrent._

import name.alex.sp.FunctionalUtils
import FunctionalUtils._

class Shop {
  private[this] val executor = Executors.newScheduledThreadPool(/* corePoolSize */ 0)

  implicit def getExecutionContext: ExecutionContext = JavaConversions.asExecutionContext(executor)

  def order(request: OrderRequest): OrderProgressCustomer = {
    val progress = new OrderProgressStaff(request)
    beginFulfilment(progress)

    progress.forCustomer
  }

  private def beginGetStaffMember(): Future[Staff] = {
    val p = Promise[Staff]()
    executor.schedule(toRunnable (() => p.success(new Staff())), 200, TimeUnit.MILLISECONDS)
    p.future
  }

  private[this] def beginFulfilment(progress: OrderProgressStaff) = {
    val f: Future[Staff] = beginGetStaffMember()
    f.onSuccess {
      case staff => staff.beginFulfilment(progress)
    }
  }

  private class Staff() {
    def beginFulfilment(progress: OrderProgressStaff) {
      executor.schedule(toRunnable (() => progress.allFinishedItems.success(Seq(CoffeeItem()))), 200, TimeUnit.MILLISECONDS)
    }
  }
}

