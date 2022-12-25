package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import kotlin.jvm.JvmStatic

@Serializable
@SerialName("hml_same")
public data class HashMapLabelSame(
    val v: Boolean,
    val n: Int
) : HashMapLabel {
    public constructor(v: Int, n: Int) : this(v != 0, n)

    override val s: BitString get() = BitString(*BooleanArray(n) { v })

    override fun toString(): String = "(hml_same\nv:$v n:$n)"

    public companion object {
        @JvmStatic
        public fun of(key: BitString, length: Int = key.size): HashMapLabelSame? {
            var zeroBitFound = false
            var oneBitFound = false
            key.forEach { bit ->
                if (bit) {
                    if (zeroBitFound) return null
                    else oneBitFound = true
                } else {
                    if (oneBitFound) return null
                    else zeroBitFound = true
                }
            }
            return HashMapLabelSame(!zeroBitFound, length)
        }
    }
}
