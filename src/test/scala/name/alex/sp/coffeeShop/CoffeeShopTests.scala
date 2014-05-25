package name.alex.sp.coffeeShop

import org.scalatest.{Matchers, FlatSpec}
import scala.concurrent.duration._
import scala.concurrent.Await

class CoffeeShopTests extends FlatSpec with Matchers {

  "Order a basic coffee" should "finish within 1 second" in {
    val request = new OrderRequest(Vector(CoffeeRequest()))
    val progress = getCoffeeShop().order(request)
    Await.ready (progress.allFinishedItems, Duration(1, SECONDS))
  }

  it should "not be immediately finished" in {
    val request = new OrderRequest(Vector(CoffeeRequest()))
    val progress = getCoffeeShop().order(request)
    val complete = progress.allFinishedItems.isCompleted

    complete should equal(false)
  }

  it should "finish with a single CoffeeItem" in {
    val request = new OrderRequest(Vector(CoffeeRequest()))
    val progress = getCoffeeShop().order(request)
    val res = Await.result (progress.allFinishedItems, Duration(1, SECONDS))

    res should have length 1
    res(0) should be(CoffeeItem())
  }

  def getCoffeeShop(): Shop = new Shop()
}