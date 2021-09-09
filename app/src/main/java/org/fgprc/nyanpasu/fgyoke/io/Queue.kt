/*
 * Copyright 2021 CHH2000day,FGPRC
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

package org.fgprc.nyanpasu.fgyoke.io

@Suppress("MemberVisibilityCanBePrivate")
class Queue<T> {
    private var head: QueueNode<T>? = null
    private var tail: QueueNode<T>? = null
    private var length: Int = 0
        get() = field

    fun isEmpty(): Boolean = length == 0

    fun enqueue(value: T) {
        synchronized(this) {
            length++
            if (isEmpty()) {
                head = QueueNode(value)
                tail = head
            } else {
                tail?.next = QueueNode(value)
                tail = tail?.next
            }
        }
    }

    fun dequeue(): T? {
        synchronized(this) {
            val result = head?.data
            head = head?.next
            if (result != null) {
                length--
            }
            return result
        }
    }

    fun getTop(): T? = head?.data

    fun clear() {
        synchronized(this) {
            head = null
            tail = null
            length = 0
        }
    }

    private class QueueNode<T>(val data: T) {
        var next: QueueNode<T>? = null
    }
}