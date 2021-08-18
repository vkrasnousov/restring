package dev.vkrasnousov.restring.internal.repository.serializer

import android.text.Spanned
import dev.vkrasnousov.restring.PluralKeyword
import dev.vkrasnousov.restring.internal.repository.model.QuantityString

/**
 * [Serializer] for quantity string resources
 */
internal object QuantityStringsSerializer : Serializer<Map<PluralKeyword, CharSequence>, String> {

    override fun serialize(value: Map<PluralKeyword, CharSequence>): String {
        return QuantityString(value, value.any { it is Spanned }).toJson()
    }

    override fun deserialize(value: String): Map<PluralKeyword, CharSequence> {
        val resource = QuantityString.fromJson(value)
        return resource.value
    }
}