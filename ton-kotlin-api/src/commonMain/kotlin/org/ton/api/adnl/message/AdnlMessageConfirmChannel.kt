@file:Suppress("PropertyName")

package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.Bits256
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter
import org.ton.tl.constructors.*

@SerialName("adnl.message.confirmChannel")
@Serializable
public data class AdnlMessageConfirmChannel(
    val key: Bits256,
    @SerialName("peer_key")
    val peerKey: Bits256,
    val date: Int
) : AdnlMessage {
    public constructor(
        key: Bits256,
        peerKey: Bits256,
        date: Instant
    ) : this(key, peerKey, date.epochSeconds.toInt())


    public fun date(): Instant = Instant.fromEpochSeconds(date.toUInt().toLong())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlMessageConfirmChannel) return false
        if (key != other.key) return false
        if (peerKey != other.peerKey) return false
        if (date != other.date) return false
        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + peerKey.hashCode()
        result = 31 * result + date
        return result
    }

    public companion object : TlConstructor<AdnlMessageConfirmChannel>(
        schema = "adnl.message.confirmChannel key:int256 peer_key:int256 date:int = adnl.Message",
    ) {
        public const val SIZE_BYTES: Int = (256 / Byte.SIZE_BYTES) * 2 + Int.SIZE_BYTES

        override fun encode(output: TlWriter, value: AdnlMessageConfirmChannel) {
            output.writeBits256(value.key)
            output.writeBits256(value.peerKey)
            output.writeInt(value.date)
        }

        override fun decode(input: TlReader): AdnlMessageConfirmChannel {
            val key = input.readBits256()
            val peerKey = input.readBits256()
            val date = input.readInt()
            return AdnlMessageConfirmChannel(key, peerKey, date)
        }
    }
}
