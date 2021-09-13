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

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.fgprc.nyanpasu.fgyoke.io.DataBuffer
import org.fgprc.nyanpasu.fgyoke.io.DataBufferPool
import org.fgprc.nyanpasu.fgyoke.io.Queue
import java.io.Closeable
import java.net.InetAddress

abstract class FlightGearConnector(
    protected val destinationAddress: InetAddress,
    protected val port: Int,
    protected val dataBufferPool: DataBufferPool
) : Closeable {
    protected open val packageQueue = Queue<DataBuffer>()
    protected open val latency = 20L
    protected open var onConnectListener: OnConnectedListener? = null
    protected open var onDisconnectListener: OnDisconnectedListener? = null
    protected open var mDaemonThread: DaemonThread = DaemonThread()
    protected var isClosed = false

    /**
     * This method should be called once the connection is established.So we can send message to server.
     */
    protected open fun onConnectionEstablished() {
        //If a thread is ready,we don't need to refresh it.
        mDaemonThread.takeIf {
            it.state == Thread.State.NEW
        } ?: run {
            //Interrupt daemon thread and start a new one
            kotlin.runCatching {
                mDaemonThread.interrupt()
            }.exceptionOrNull().let {
                it?.printStackTrace()
            }
            mDaemonThread = DaemonThread()
            mDaemonThread
        }.start()
    }

    /**
     * Connect to server.
     */
    abstract fun connect()

    /**
     * Only interrupt daemon thread
     * DOES NOT CHANGES [isClosed] FLAG!
     */
    override fun close() {
        if (isClosed) {
            throw IllegalStateException("Connection is already closed")
        }
        if (mDaemonThread.isAlive) {
            mDaemonThread.interrupt()
        }
    }

    open fun setOnConnectedListener(listener: OnConnectedListener) {
        onConnectListener = listener
    }

    open fun setOnDisconnectedListener(listener: OnDisconnectedListener) {
        onDisconnectListener = listener
    }

    /**
     * Enqueues a DataBuffer,wait for daemon thread to take it.
     */
    fun enqueuePackage(buffer: DataBuffer) {
        packageQueue.enqueue(buffer)
    }

    /**
     * Sends a message to the server.
     */
    protected abstract suspend fun sendPackage(buffer: DataBuffer)

    protected inner class DaemonThread : Thread("ConnectorDaemonThread") {
        override fun run() {
            super.run()
            runBlocking {
                //Breaks main loop as soon as an exception is thrown
                runCatching {
                    mainLoop@ while (!isInterrupted) {
                        delay(latency)
                        //Try to get a message to send to server
                        val content = packageQueue.dequeue()
                        content?.let {
                            //Send package to server and release it
                            sendPackage(it)
                            dataBufferPool.releaseBuffer(it)
                        }
                    }
                }.exceptionOrNull()?.printStackTrace()
            }
        }
    }

    fun interface OnConnectedListener {
        suspend fun onConnected()
    }

    fun interface OnDisconnectedListener {
        suspend fun onDisconnected(reason: Throwable?)
    }
}