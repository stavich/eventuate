/*
 * Copyright (C) 2015 Red Bull Media House GmbH <http://www.redbullmediahouse.com> - all rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rbmhtechnology

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.control.NoStackTrace

package object eventuate {
  implicit class AwaitHelper[T](w: Awaitable[T]) {
    def await: T = Await.result(w, 10.seconds)
  }

  val boom = new Exception("boom") with NoStackTrace
}