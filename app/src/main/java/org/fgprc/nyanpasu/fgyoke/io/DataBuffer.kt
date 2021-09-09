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

@file:Suppress("DataClassPrivateConstructor")

package org.fgprc.nyanpasu.fgyoke.io

import java.nio.ByteBuffer
import java.nio.ByteOrder

data class DataBuffer private constructor(
    private val keyArray: Array<String>,
    private val valueArray: FloatArray,
    var isDataReady: Boolean = false,
    val packageId: Int
) {
    private val buffer = ByteBuffer.allocate(valueArray.size * Float.SIZE_BYTES).also {
        //Set to little endian
        it.order(ByteOrder.LITTLE_ENDIAN)
    }

    companion object {
        var id = 0
        private fun nextId(): Int = id++
    }

    constructor(keyList: List<String>) : this(
        keyList.toTypedArray(),
        FloatArray(keyList.size), packageId = nextId()
    ) {
        for (i in valueArray.indices) {
            valueArray[i] = Float.NaN
        }
    }

    fun release() {
        isDataReady = false
    }

    fun setData(vararg parameters: Float) {
        if (parameters.size != valueArray.size) {
            throw IllegalArgumentException("Parameter size disagree!")
        }
        for (i in parameters.indices) {
            valueArray[i] = parameters[i]
        }
        isDataReady = true
    }

    fun toByteArray(): ByteArray {
        valueArray.forEachIndexed { index, f ->
            buffer.putFloat(index * Float.SIZE_BYTES, f)
        }
        return buffer.array()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataBuffer

        if (packageId != other.packageId) return false

        return true
    }

    override fun hashCode(): Int {
        return packageId
    }
}
