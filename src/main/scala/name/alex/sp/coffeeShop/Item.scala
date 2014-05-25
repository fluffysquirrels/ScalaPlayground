package name.alex.sp.coffeeShop

sealed abstract class Item
sealed case class CoffeeItem() extends Item
sealed case class PrePreparedItem() extends Item