package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

@SerialName("ahme_empty")
@Serializable
public data class AugDictionaryEmpty<out X, out Y>(
    override val extra: Y
) : AugDictionary<X, Y> {
    override fun nodes(): Sequence<Pair<X, Y>> = emptySequence()

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("ahme_empty") {
        field("extra", extra)
    }

    override fun toString(): String = print().toString()

    public companion object {
        @JvmStatic
        public fun <X, Y> tlbCodec(
            y: TlbCodec<Y>
        ): TlbConstructor<AugDictionaryEmpty<X, Y>> = AugDictionaryEmptyTlbConstructor(y)
    }
}

internal class AugDictionaryEmptyTlbConstructor<X, Y>(
    val y: TlbCodec<Y>,
) : TlbConstructor<AugDictionaryEmpty<X, Y>>(
    schema = "ahme_empty\$0 {n:#} {X:Type} {Y:Type} extra:Y = AugDictionary n X Y;",
    id = ID
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AugDictionaryEmpty<X, Y>
    ) = cellBuilder {
        storeTlb(y, value.extra)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AugDictionaryEmpty<X, Y> = cellSlice {
        val extra = loadTlb(y)
        AugDictionaryEmpty(extra)
    }

    companion object {
        val ID = BitString(false)
    }
}
