package name.alex.sp

import java.util.concurrent.atomic.AtomicInteger

class Counter {
  private[this] val currentValue = new AtomicInteger(1)
  def getNextValue() = currentValue.getAndIncrement()
}
