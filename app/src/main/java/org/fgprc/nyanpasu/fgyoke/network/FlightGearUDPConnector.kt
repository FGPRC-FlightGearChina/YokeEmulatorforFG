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

import kotlinx.coroutines.suspendCancellableCoroutine
import org.fgprc.nyanpasu.fgyoke.io.DataBuffer
import org.fgprc.nyanpasu.fgyoke.io.DataBufferPool
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

open class FlightGearUDPConnector(
    destinationAddress: InetAddress,
    port: Int,
    dataBufferPool: DataBufferPool
) :
    FlightGearConnector(destinationAddress, port, dataBufferPool) {
    @Suppress("MemberVisibilityCanBePrivate")
    protected var socket: DatagramSocket? = null
    override fun connect() {
        socket = DatagramSocket()
    }

    /**
     * Sends a message to the server.
     */
    override suspend fun sendPackage(buffer: DataBuffer) {
        if (isClosed) {
            throw IllegalStateException("Connection already closed")
        }
        suspendCancellableCoroutine<Unit> {
            val data = buffer.toByteArray()
            val datagramPackage = DatagramPacket(data, data.size, destinationAddress, port)
            socket?.send(datagramPackage)
        }
    }

    override fun close() {
        super.close()
        socket?.close()
        isClosed = true
    }
}