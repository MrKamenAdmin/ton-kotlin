@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.crypto.HexByteArraySerializer
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.asTlbCombinator

@Serializable
@SerialName("update_hashes")
data class HashUpdate(
    val old_hash: BitString,
    val new_hash: BitString
) {
    override fun toString(): String = buildString {
        append("(update_hashes\n")
        append("old_hash:$old_hash")
        append(" new_hash:$new_hash")
        append(")")
    }

    companion object : TlbCodec<HashUpdate> by HashUpdateTlbConstructor.asTlbCombinator()
}

private object HashUpdateTlbConstructor : TlbConstructor<HashUpdate>(
    schema = "update_hashes#72 {X:Type} old_hash:bits256 new_hash:bits256 = HASH_UPDATE X;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: HashUpdate
    ) = cellBuilder {
        storeBits(value.old_hash)
        storeBits(value.new_hash)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): HashUpdate = cellSlice {
        val oldHash = loadBits(256)
        val newHash = loadBits(256)
        HashUpdate(oldHash, newHash)
    }
}
