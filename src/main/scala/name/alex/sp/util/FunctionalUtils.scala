package name.alex.sp.util

object FunctionalUtils {
  def toRunnable(fun : () => Unit) = new Runnable() { def run() = fun() }
}
