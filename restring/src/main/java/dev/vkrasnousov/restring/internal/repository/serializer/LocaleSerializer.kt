package dev.vkrasnousov.restring.internal.repository.serializer

import dev.vkrasnousov.restring.internal.repository.util.LocaleUtil
import java.util.*

/**
 * [Serializer] for [Locale]s
 */
internal object LocaleSerializer : Serializer<Locale, String> {

    override fun serialize(value: Locale): String {
        return LocaleUtil.toLanguageTag(value)
    }

    override fun deserialize(value: String): Locale {
        return LocaleUtil.fromLanguageTag(value)
    }
}