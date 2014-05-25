package name.alex.sp.coffeeShop

sealed abstract trait CoffeeType
sealed case class Americano() extends CoffeeType
sealed case class Cappuccino() extends CoffeeType
sealed case class FlatWhite() extends CoffeeType
sealed case class Latte() extends CoffeeType