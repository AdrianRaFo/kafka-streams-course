package kstreaming.rockjvm

import adrianrafo.kstreaming.rockjvm.Domain._
import org.apache.kafka.streams.kstream.{GlobalKTable, JoinWindows}
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.serialization.Serdes._

import java.time.Duration
import java.time.temporal.ChronoUnit

object Topology {

  def paidOrders(builder: StreamsBuilder): Unit = {

    val usersOrdersStreams: KStream[UserId, Order] = builder.stream[UserId, Order](Topics.OrdersByUser)

    val userProfilesTable: KTable[UserId, Profile] =
      builder.table[UserId, Profile](Topics.DiscountProfilesByUser)

    val discountProfilesGTable: GlobalKTable[Profile, Discount] =
      builder.globalTable[Profile, Discount](Topics.Discounts)

    val ordersWithUserProfileStream: KStream[UserId, (Order, Profile)] =
      usersOrdersStreams.join[Profile, (Order, Profile)](userProfilesTable) { (order, profile) =>
        (order, profile)
      }

    val discountedOrdersStream: KStream[UserId, Order] =
      ordersWithUserProfileStream.join[Profile, Discount, Order](discountProfilesGTable)(
        { case (_, (_, profile)) => profile }, // Joining key
        { case ((order, _), discount) => order.copy(amount = order.amount * discount.amount) }
      )

    val ordersStream: KStream[OrderId, Order] = discountedOrdersStream.selectKey { (_, order) => order.orderId }

    val paymentsStream: KStream[OrderId, Payment] = builder.stream[OrderId, Payment](Topics.Payments)

    val paidOrders: KStream[OrderId, Order] = {

      val joinOrdersAndPayments = (order: Order, payment: Payment) =>
        if (payment.status == "PAID") Option(order) else Option.empty[Order]

      val joinWindow = JoinWindows.ofTimeDifferenceWithNoGrace(Duration.of(5, ChronoUnit.MINUTES))

      ordersStream.join[Payment, Option[Order]](paymentsStream)(joinOrdersAndPayments, joinWindow)
        .flatMapValues(maybeOrder => maybeOrder.toList)
    }

    paidOrders.to(Topics.PaidOrders)
  }

}
