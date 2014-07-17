package name.alex.sp.util

import java.util.concurrent.atomic.AtomicInteger

class Counter {
  private[this] val currentValue = new AtomicInteger(1)
  def getNextValue() = currentValue.getAndIncrement()
}
