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

package org.fgprc.nyanpasu.fgyoke.network

import kotlinx.coroutines.*
import org.fgprc.nyanpasu.fgyoke.io.DataBuffer
import org.fgprc.nyanpasu.fgyoke.io.DataBufferPool
import java.net.InetAddress
import java.net.Socket

class FlightGearTCPConnector(
    destinationAddress: InetAddress,
    port: Int,
    dataBufferPool: DataBufferPool
) :
    FlightGearConnector(destinationAddress, port, dataBufferPool) {
    private var socket: Socket? = null

    /**
     * Connect to server.
     */
    @OptIn(DelicateCoroutinesApi::class)
    override fun connect() {
        synchronized(this) {
            if (socket == null) {
                try {
                    socket = Socket(destinationAddress, port)
                    outputStream = socket!!.getOutputStream()
                    //Starts daemon thread
                    onConnectionEstablished()
                    GlobalScope.launch {
                        onConnectListener?.onConnected()
                    }
                } catch (t: Throwable) {
                    runBlocking {
                        onDisconnectListener?.onDisconnected(t)
                    }
                }
            }
        }
    }

    /**
     * Sends a message to the server.
     */
    override suspend fun sendPackage(buffer: DataBuffer) {
        if (isClosed) {
            throw IllegalStateException("Connection already closed")
        }
        suspendCancellableCoroutine<Unit> {
            outputStream.write(buffer.toByteArray())
        }
    }

    override fun close() {
        super.close()
        outputStream.close()
        socket?.close()
        isClosed = true
    }
}