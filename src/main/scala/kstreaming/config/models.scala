package kstreaming.config

final case class SchemaRegistry(uri: String, cachedSchemasPerSubject: Int)

final case class BrokerAddress(uri: String)

case class ServiceConfig(kafka: KafkaConfig)

case class KafkaConfig(server: BrokerAddress, schemaRegistry: SchemaRegistry)
