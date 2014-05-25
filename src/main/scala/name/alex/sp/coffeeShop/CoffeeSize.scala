package name.alex.sp.coffeeShop

sealed abstract trait CoffeeSize
sealed case class Small() extends CoffeeSize
sealed case class Medium() extends CoffeeSize
sealed case class Large() extends CoffeeSize

