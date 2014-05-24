package name.alex.sp.coffeeShop

abstract class ItemRequest
case class CoffeeRequest() extends ItemRequest
case class PrePreparedItemRequest() extends ItemRequest