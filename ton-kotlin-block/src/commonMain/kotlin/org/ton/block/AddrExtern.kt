package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("addr_extern")
data class AddrExtern(
    val len: Int,
    val external_address: BitString
) : MsgAddressExt {
    init {
        require(external_address.size == len) { "required: external_address.size == len, actual: ${external_address.size}" }
    }

    constructor(externalAddress: ByteArray) : this(externalAddress.toBitString())
    constructor(externalAddress: BitString) : this(externalAddress.size, externalAddress)

    override fun toString(): String = buildString {
        append("(addr_extern\n")
        append("len:")
        append(len)
        append(" external_address:")
        append(external_address)
        append(")")
    }

    companion object : TlbConstructorProvider<AddrExtern> by AddrExternTlbConstructor
}

private object AddrExternTlbConstructor : TlbConstructor<AddrExtern>(
    schema = "addr_extern\$01 len:(## 9) external_address:(bits len) = MsgAddressExt;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: AddrExtern
    ) = cellBuilder {
        storeUInt(value.len, 9)
        storeBits(value.external_address)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AddrExtern = cellSlice {
        val len = loadUInt(9).toInt()
        val externalAddress = loadBits(len)
        AddrExtern(len, externalAddress)
    }
}
