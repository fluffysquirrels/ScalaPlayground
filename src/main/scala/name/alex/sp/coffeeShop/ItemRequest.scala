package name.alex.sp.coffeeShop

sealed abstract class ItemRequest
sealed case class CoffeeRequest(val coffeeType: CoffeeType, val coffeeSize: CoffeeSize) extends ItemRequest
sealed case class PrePreparedItemRequest() extends ItemRequest