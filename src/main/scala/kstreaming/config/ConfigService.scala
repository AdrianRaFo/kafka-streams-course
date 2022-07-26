package kstreaming.config

import cats.effect._
import cats.implicits._
import com.typesafe.config.ConfigFactory
import fs2.kafka.{AdminClientSettings, KafkaAdminClient}
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.errors.TopicExistsException
import org.apache.kafka.streams.scala.serialization.Serdes
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}
import org.typelevel.log4cats.Logger
import pureconfig._
import pureconfig.generic.auto._

import java.util.Properties
import scala.jdk.CollectionConverters._

trait ConfigService[F[_]] {
  def createNewTopic(topicName: String, cleanupPolicy: Boolean): F[Unit]
  def createKafkaStreamsApp(topology: Topology): F[KafkaStreams]
}
object ConfigService {

  def impl[F[_]: Async](logger: Logger[F]): F[ConfigService[F]] =
    for {
      config <- Sync[F].delay(ConfigSource.fromConfig(ConfigFactory.defaultApplication()).loadOrThrow[ServiceConfig])
//      schemaRegistry <- SchemaRegistryClientSettings[F](config.kafka.schemaRegistry.uri)
//        .withMaxCacheSize(config.kafka.schemaRegistry.cachedSchemasPerSubject)
//        .createSchemaRegistryClient
//        .map(AvroSettings(_))
    } yield new ConfigService[F] {

      def createNewTopic(topicName: String, cleanupPolicy: Boolean): F[Unit] = {
        val adminClientSettings: AdminClientSettings = AdminClientSettings(config.kafka.server.uri)

        KafkaAdminClient.resource(adminClientSettings).use {
          val topic = new NewTopic(topicName, 1, 1.toShort)
          val finalTopic =
            if (cleanupPolicy) topic.configs(Map("cleanup.policy" -> "compact").asJava)
            else topic

          _.createTopic(finalTopic).recoverWith { case _: TopicExistsException =>
            logger.info(s"Topic $topicName already exists")
          }.void
        }
      }

      override def createKafkaStreamsApp(topology: Topology): F[KafkaStreams] = {
        val props = new Properties
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "orders-application")
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafka.server.uri)
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.stringSerde.getClass)

        Sync[F].delay(new KafkaStreams(topology, props))
      }

    }

}
