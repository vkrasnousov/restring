package dev.vkrasnousov.restring.internal

import dev.vkrasnousov.restring.LocaleProvider
import java.util.*

/**
 * Default implementation of [LocaleProvider]
 */
internal object DefaultLocaleProvider : LocaleProvider {

    override var isInitial = true

    override var currentLocale: Locale = Locale.getDefault()
        get() {
            if (isInitial) {
                return Locale.getDefault()
            }
            return field
        }
        set(value) {
            field = value
            isInitial = false
        }
}