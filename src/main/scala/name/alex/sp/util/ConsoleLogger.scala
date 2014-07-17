package name.alex.sp.util

import java.text.SimpleDateFormat
import java.util.Calendar

class ConsoleLogger(val name: String) {

  private val timeFormat = new SimpleDateFormat("HH:mm:ss.SSS")

  def log(msg: String) {
    val now = Calendar.getInstance().getTime()
    val nowString = timeFormat.format(now)
    println(f"$nowString - $name: $msg")
  }
}
