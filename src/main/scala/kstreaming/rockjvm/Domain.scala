package kstreaming.rockjvm

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

object Domain {
  type UserId  = String
  type Profile = String
  type Product = String
  type OrderId = String

  case class Order(orderId: OrderId, user: UserId, products: List[Product], amount: Double)
  object Order {
    implicit val codec: Codec.AsObject[Order] = deriveCodec
  }

  case class Discount(profile: Profile, amount: Double)  // in percentage points
  object Discount {
    implicit val codec: Codec.AsObject[Discount] = deriveCodec
  }

  case class Payment(orderId: OrderId, status: String)
  object Payment {
    implicit val codec: Codec.AsObject[Payment] = deriveCodec
  }

}
