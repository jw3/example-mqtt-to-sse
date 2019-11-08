package com.github.jw3.m2s

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.sse.EventStreamMarshalling._
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.http.scaladsl.server.Directives.{complete, get, path, _}
import akka.http.scaladsl.server.Route
import com.github.jw3.m2s.mqttosse.SourceFactory

import scala.concurrent.duration.DurationInt

trait HttpInterface {
  implicit def system: ActorSystem

  def routes(f: SourceFactory): Route =
    path("health") {
      get {
        complete(OK)
      }
    } ~
    pathPrefix("topics") {
      path("topic" / Segment) { topic =>
        get {
          extractRequest { r =>
            complete {
              f(s"$topic-${}", Seq(topic))
                .map(m => ServerSentEvent(m.payload.utf8String, Some(m.topic)))
                .keepAlive(1.second, () => ServerSentEvent.heartbeat)
            }
          }
        }
      }
    }
}
