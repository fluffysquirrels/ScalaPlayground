package name.alex.sp.coffeeShop

class Shop {
  def order(request: OrderRequest): OrderProgress = {
    return new OrderProgress()
  }
}
