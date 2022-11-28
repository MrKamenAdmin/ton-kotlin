@file:Suppress("NOTHING_TO_INLINE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bigint.toBigInt
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.crypto.base64
import org.ton.crypto.base64url
import org.ton.crypto.crc16
import org.ton.crypto.hex
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.experimental.and
import kotlin.experimental.or

inline fun AddrStd(address: String): AddrStd = AddrStd.parse(address)

@Serializable
@SerialName("addr_std")
data class AddrStd(
    val anycast: Maybe<Anycast>,
    override val workchain_id: Int,
    val address: BitString
) : MsgAddressInt {
    init {
        require(address.size == 256) { "address.size expected: 256 actual: ${address.size}" }
    }

    constructor(workchainId: Int, address: BitString) : this(null, workchainId, address)
    constructor(workchainId: Int, address: ByteArray) : this(null, workchainId, address)
    constructor(anycast: Anycast?, workchainId: Int, address: ByteArray) : this(
        anycast.toMaybe(),
        workchainId,
        address.toBitString()
    )

    constructor(anycast: Anycast?, workchainId: Int, address: BitString) : this(
        anycast.toMaybe(),
        workchainId,
        address
    )

    override fun toString(): String = "addr_std(anycast:$anycast workchain_id:$workchain_id address:$address)"

    fun toString(
        userFriendly: Boolean = true,
        urlSafe: Boolean = true,
        testOnly: Boolean = false,
        bounceable: Boolean = true
    ): String = toString(this, userFriendly, urlSafe, testOnly, bounceable)

    companion object : TlbCodec<AddrStd> by AddrStdTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<AddrStd> = AddrStdTlbConstructor

        @JvmStatic
        fun toString(
            address: AddrStd,
            userFriendly: Boolean = true,
            urlSafe: Boolean = true,
            testOnly: Boolean = false,
            bounceable: Boolean = true
        ): String {
            return if (userFriendly) {
                val raw = byteArrayOf(tag(testOnly, bounceable), address.workchain_id.toByte()) +
                        address.address.toByteArray() + crc(address, testOnly, bounceable).toBigInt()
                    .toByteArray()
                if (urlSafe) {
                    base64url(raw)
                } else {
                    base64(raw)
                }
            } else {
                address.workchain_id.toString() + ":" + hex(address.address.toByteArray())
            }
        }

        @JvmStatic
        fun parse(address: String): AddrStd = try {
            if (address.contains(':')) {
                parseRaw(address)
            } else {
                parseUserFriendly(address)
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Can't parse address: $address", e)
        }

        @JvmStatic
        fun parseRaw(address: String): AddrStd {
            require(address.contains(':'))
            // 32 bytes, each represented as 2 characters
            require(address.substringAfter(':').length == 32 * 2)
            return AddrStd(
                // toByte() to make sure it fits into 8 bits
                workchainId = address.substringBefore(':').toByte().toInt(),
                address = hex(address.substringAfter(':'))
            )
        }

        @JvmStatic
        fun parseUserFriendly(address: String): AddrStd {
            val raw = try {
                base64url(address)
            } catch (E: Exception) {
                base64(address)
            }

            require(raw.size == 36) { "invalid byte-array size expected: 36, actual: ${raw.size}" }
            // not 0x80 = 0x7F; here we clean the test only flag to only check proper bounce flags
            val cleanTestOnly = raw[0] and 0x7F.toByte()
            check((cleanTestOnly == 0x11.toByte()) or (cleanTestOnly == 0x51.toByte())) {
                "unknown address tag"
            }

            val addrStd = AddrStd(
                workchainId = raw[1].toInt(),
                address = raw.sliceArray(2..33)
            )

            val testOnly = raw[0] and 0x80.toByte() != 0.toByte()
            val bounceable = cleanTestOnly == 0x11.toByte()
            val expectedChecksum = raw[34].toUByte().toInt() * 256 + raw[35].toUByte().toInt()
            val actualChecksum = crc(addrStd, testOnly, bounceable)
            check(expectedChecksum == actualChecksum) {
                "CRC check failed"
            }

            return addrStd
        }

        private fun crc(address: AddrStd, testOnly: Boolean, bounceable: Boolean): Int =
            crc16(
                byteArrayOf(tag(testOnly, bounceable), address.workchain_id.toByte()),
                address.address.toByteArray()
            )

        // Get the tag byte based on set flags
        private fun tag(testOnly: Boolean, bounceable: Boolean): Byte =
            (if (testOnly) 0x80.toByte() else 0.toByte()) or
                    (if (bounceable) 0x11.toByte() else 0x51.toByte())
    }
}

private object AddrStdTlbConstructor : TlbConstructor<AddrStd>(
    schema = "addr_std\$10 anycast:(Maybe Anycast) workchain_id:int8 address:bits256 = MsgAddressInt;"
) {
    private val MaybeAnycast = Maybe(Anycast)

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AddrStd
    ) = cellBuilder {
        storeTlb(MaybeAnycast, value.anycast)
        storeInt(value.workchain_id, 8)
        storeBits(value.address)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AddrStd = cellSlice {
        val anycast = loadTlb(MaybeAnycast)
        val workchainId = loadInt(8).toInt()
        val address = loadBits(256)
        AddrStd(anycast, workchainId, address)
    }
}
