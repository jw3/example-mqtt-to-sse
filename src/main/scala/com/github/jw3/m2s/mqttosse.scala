package com.github.jw3.m2s

import akka.Done
import akka.stream.alpakka.mqtt
import akka.stream.alpakka.mqtt.scaladsl.MqttSource
import akka.stream.alpakka.mqtt.{MqttConnectionSettings, MqttMessage, MqttQoS}
import akka.stream.scaladsl.Tcp.OutgoingConnection
import akka.stream.scaladsl.{Flow, Source}
import akka.util.ByteString

import scala.concurrent.Future

object mqttosse {
  type MQTTConnection = Flow[ByteString, ByteString, Future[OutgoingConnection]]
  type SourceFactory = (String, Seq[String]) => Source[MqttMessage, Future[Done]]

  def source(cfg: MqttConnectionSettings, cid: String, sub: Seq[String]): Source[MqttMessage, Future[Done]] =
    MqttSource.atMostOnce(
      cfg.withClientId(clientId = cid),
      mqtt.MqttSubscriptions(sub.map(_ -> MqttQoS.atLeastOnce).toMap),
      bufferSize = 8
    )
}
