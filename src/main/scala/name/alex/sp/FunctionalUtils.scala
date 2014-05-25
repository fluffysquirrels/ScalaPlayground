package name.alex.sp

object FunctionalUtils {
  def toRunnable(fun : () => Unit) = new Runnable() { def run() = fun() }
}
