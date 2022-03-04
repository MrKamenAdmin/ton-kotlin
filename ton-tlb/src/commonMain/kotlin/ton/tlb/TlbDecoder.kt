package ton.tlb

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import ton.bitstring.BitString
import ton.bitstring.BitStringReader
import ton.tlb.types.InbuiltTypeFactory
import ton.tlb.types.TonTypeFactory
import ton.tlb.types.TypeCombinator

data class TlbDecoder(
    val cell: Cell,
    val parent: TlbDecoder? = null,
    val reader: BitStringReader = BitStringReader(cell.data),
) : InbuiltTypeFactory, TonTypeFactory {
    var cellRefPointer = 0

    fun decode(typeCombinator: TypeCombinator) = typeCombinator.decode(this)

    fun decodeToJson(typeCombinator: TypeCombinator) = (decode(typeCombinator) as Map<*, *>).toJson()

    private fun Map<*, *>.toJson(): JsonObject = buildJsonObject {
        forEach { (field, value) ->
            val key = field.toString()
            val element = when (value) {
                is Map<*, *> -> value.toJson()
                "bool_true" -> JsonPrimitive(true)
                "bool_false" -> JsonPrimitive(false)
                is String -> JsonPrimitive(value)
                is Number -> JsonPrimitive(value)
                is BitString -> JsonPrimitive(value.toString())
                else -> JsonNull
            }
            put(key, element)
        }
    }
}