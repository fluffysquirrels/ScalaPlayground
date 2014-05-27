package name.alex.sp.coffeeShop

import java.util.concurrent.{ScheduledExecutorService, TimeUnit, Executors}
import scala.concurrent._

import name.alex.sp.{Counter, ConsoleLogger, FunctionalUtils}
import FunctionalUtils._
import java.util.concurrent.atomic.AtomicInteger

class Shop {
  private[this] val executor = Executors.newScheduledThreadPool(/* corePoolSize */ 0)
  implicit def getExecutionContext: ExecutionContext = JavaConversions.asExecutionContext(executor)
  private[this] val shopLogger = new ConsoleLogger("Coffee shop")

  def order(request: OrderRequest): OrderProgressCustomer = {
    val progress = new OrderProgressStaff(request)
    beginFulfilment(progress)

    progress.forCustomer
  }

  private def beginGetStaffMember(): Future[StaffMember] = {
    val p = Promise[StaffMember]()
    executor.schedule(toRunnable (() => {
      val s = new StaffMember()
      shopLogger log f"Got staff member ${s.number}"
      p.success(s)
    }), 200, TimeUnit.MILLISECONDS)
    p.future
  }

  private[this] def beginFulfilment(progress: OrderProgressStaff): Unit = {
    shopLogger log f"Begin fulfilment for order ${progress.number}"
    val f: Future[StaffMember] = beginGetStaffMember()
    f.onSuccess {
      case staff => staff.beginFulfilment(progress)
    }
  }

  private class StaffMember() {
    val number = StaffMember.staffCounter.getNextValue()
    private[this] val staffLogger = new ConsoleLogger(f"Staff member $number")
    staffLogger log "Created"

    def fulfil(progress: OrderProgressStaff) {
      staffLogger log f"Completed fulfilment on order ${progress.number}"
      progress.allFinishedItems.success(Seq(CoffeeItem()))
    }

    def beginFulfilment(progress: OrderProgressStaff) {
      staffLogger log s"Begin fulfilment on order ${progress.number}"
      executor.schedule(toRunnable (() => fulfil(progress)), 200, TimeUnit.MILLISECONDS)
    }
  }

  object StaffMember {
    private val staffCounter = new Counter()
  }
}

