/*
 * Copyright 2015 - 2016 Red Bull Media House GmbH <http://www.redbullmediahouse.com> - all rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rbmhtechnology.eventuate.adapter.vertx

import com.rbmhtechnology.eventuate.DurableEvent
import io.vertx.core.Vertx
import io.vertx.core.eventbus.{ Message, MessageConsumer }

object Event {
  def apply(message: Message[DurableEvent]): Event =
    new Event(message, message.body().localSequenceNr, message.body().payload)

  def withConfirmation(message: Message[DurableEvent], logAdapterInfo: SendLogAdapterInfo, vertx: Vertx): ConfirmableEvent =
    new ConfirmableEvent(message, message.body().localSequenceNr, message.body().payload, logAdapterInfo, vertx)
}

case class Event(message: Message[DurableEvent], id: Long, payload: Any)

class ConfirmableEvent private[vertx] (message: Message[DurableEvent], id: Long, payload: Any, private val logAdapterInfo: SendLogAdapterInfo, private val vertx: Vertx)
  extends Event(message, id, payload) {

  def confirm(): Unit = {
    message.reply(null)
  }
}

object ConfirmableEvent {
  def unapply(arg: ConfirmableEvent): Option[(Long, Any)] =
    Some((arg.id, arg.payload))
}

object EventSubscription {
  def apply(messageConsumer: MessageConsumer[DurableEvent]): EventSubscription =
    new EventSubscription(messageConsumer)
}

class EventSubscription(messageConsumer: MessageConsumer[DurableEvent]) {

  def unsubscribe(): Unit = {
    messageConsumer.unregister()
  }

  def pause(): Unit = {
    messageConsumer.pause()
  }

  def resume(): Unit = {
    messageConsumer.resume()
  }
}
