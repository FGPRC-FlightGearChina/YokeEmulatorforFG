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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class DataBufferPool(private val keyList: List<String>) {
    companion object {
        const val poolSize = 20
    }

    private val bufferQueue = Queue<DataBuffer>()
    private val bufferArray = Array(poolSize) { DataBuffer(keyList) }

    init {
        for (i in bufferArray.indices) {
            bufferQueue.enqueue(bufferArray[i])
        }
    }

    suspend fun getBuffer(): DataBuffer = withContext(Dispatchers.IO) {
        var buffer: DataBuffer? = bufferQueue.dequeue()
        //If needed,wait until a buffer is available
        while (buffer == null) {
            delay(20)
            buffer = bufferQueue.dequeue()
        }
        return@withContext buffer
    }

    suspend fun releaseBuffer(buffer: DataBuffer) = withContext(Dispatchers.IO) {
        if (!buffer.isInPool()) {
            throw IllegalStateException("DataBuffer:$buffer SHALL NOT in pool")
        }
        buffer.release()
        bufferQueue.enqueue(buffer)
    }

    private fun DataBuffer.isInPool(): Boolean = bufferArray.contains(this)
}