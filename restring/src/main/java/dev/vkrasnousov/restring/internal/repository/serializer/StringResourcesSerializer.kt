package dev.vkrasnousov.restring.internal.repository.serializer

import android.text.Spanned
import dev.vkrasnousov.restring.internal.repository.model.StringResource

/**
 * [Serializer] for string resources
 */
internal object StringResourcesSerializer : Serializer<CharSequence, String> {

    override fun serialize(value: CharSequence): String {
        return StringResource(value, value is Spanned).toJson()
    }

    override fun deserialize(value: String): CharSequence {
        val stringResource = StringResource.fromJson(value)
        return stringResource.value

    }
}