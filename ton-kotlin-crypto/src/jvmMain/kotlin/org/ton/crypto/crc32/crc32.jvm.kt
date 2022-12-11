package org.ton.crypto.crc32

import java.util.zip.CRC32

// using CRC32 from java.util.zip due native implementation
actual fun crc32(bytes: ByteArray, fromIndex: Int, toIndex: Int): Int = CRC32().apply {
    update(bytes, fromIndex, toIndex)
}.value.toInt()

// using basic implementation due CRC32C from java.util.zip is not supported on JVM 1.8
actual fun crc32c(bytes: ByteArray, fromIndex: Int, toIndex: Int): Int =
    crc32slicingUpdate(0, CASTAGNOLI_TABLE, bytes, fromIndex, toIndex)
