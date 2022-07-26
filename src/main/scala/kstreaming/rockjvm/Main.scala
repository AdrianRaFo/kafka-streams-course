package adrianrafo.kstreaming.rockjvm

import adrianrafo.kstreaming.config.ConfigService
import cats.effect.{IO, IOApp}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp.Simple {
  override def run: IO[Unit] = for {
    logger <- Slf4jLogger.create[IO]
    config <- ConfigService.impl[IO](logger)

    _ <- config.createNewTopic(Topics.OrdersByUser, cleanupPolicy = false)
    _ <- config.createNewTopic(Topics.DiscountProfilesByUser, cleanupPolicy = true)
    _ <- config.createNewTopic(Topics.Discounts, cleanupPolicy = true)
    _ <- config.createNewTopic(Topics.Orders, cleanupPolicy = false)
    _ <- config.createNewTopic(Topics.Payments, cleanupPolicy = false)
    _ <- config.createNewTopic(Topics.PaidOrders, cleanupPolicy = false)

    builder  = new StreamsBuilder
    _        = Topology.paidOrders(builder)
    topology = builder.build()

    _ = println(topology.describe())

    application <- config.createKafkaStreamsApp(topology)
    _           <- IO(application.start())
    _           <- IO.never[Unit]
  } yield ()
}
