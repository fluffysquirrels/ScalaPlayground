package name.alex.sp.coffeeShop

import org.scalatest.{Matchers, FlatSpec}
import scala.concurrent.duration._
import scala.concurrent.Await

class CoffeeShopTests extends FlatSpec with Matchers {

  "Order a basic coffee" should "finish within 1 second" in {
    val progress = getCoffeeShop().order(createBasicCoffeeRequest())
    Await.ready (progress.allFinishedItems, Duration(1, SECONDS))
  }

  it should "not be immediately finished" in {
    val progress = getCoffeeShop().order(createBasicCoffeeRequest())
    val complete = progress.allFinishedItems.isCompleted

    complete should equal(false)
  }

  it should "finish with a single CoffeeItem" in {
    val progress = getCoffeeShop().order(createBasicCoffeeRequest())
    val res = Await.result (progress.allFinishedItems, Duration(1, SECONDS))

    res should have length 1
    res(0) should be(CoffeeItem())
  }

  def createBasicCoffeeRequest() = new OrderRequest(Vector(CoffeeRequest(Americano(), Large())))

  def getCoffeeShop(): Shop = new Shop()
}