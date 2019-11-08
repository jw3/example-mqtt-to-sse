package com.github.jw3.m2s

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.stream.alpakka.mqtt.MqttConnectionSettings
import net.ceedubs.ficus.Ficus._
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

object Boot extends App with HttpInterface {
  implicit val system: ActorSystem = ActorSystem("mqttosse")
  implicit val mat: ActorMaterializer = ActorMaterializer()

  val config = system.settings.config

  val brokerHost = config.as[String]("mqtt.host")
  val brokerPort = config.as[Int]("mqtt.port")
  val connectionSettings = MqttConnectionSettings(
    s"tcp://$brokerHost:$brokerPort",
    "__unused__", // replaced per connection
    new MemoryPersistence
  )

  val httpHost = config.as[String]("http.host")
  val httpPort = config.as[Int]("http.port")
  Http().bindAndHandle(routes(mqttosse.source(connectionSettings, _, _)), httpHost, httpPort)
}
