package kstreaming

import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, jawn}
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.serialization.Serdes

package object rockjvm {
  implicit def serde[A >: Null : Decoder : Encoder]: Serde[A] = {
    val serializer = (a: A) => a.asJson.noSpaces.getBytes
    val deserializer = (aAsBytes: Array[Byte]) => {
      val aAsString = new String(aAsBytes)
      val aOrError = jawn.decode[A](aAsString)
      aOrError match {
        case Right(a) => Option(a)
        case Left(error) =>
          println(s"There was an error converting the message $aOrError, $error")
          Option.empty
      }
    }
    Serdes.fromFn[A](serializer, deserializer)
  }
}
