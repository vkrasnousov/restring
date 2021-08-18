package dev.vkrasnousov.restring.internal

import android.content.Context
import android.content.res.Resources
import android.os.Build
import dev.vkrasnousov.restring.PluralKeyword
import dev.vkrasnousov.restring.Restring
import dev.vkrasnousov.restring.StringRepository
import java.util.*

/**
 * A delegate class for [Resources]. Handles all resource calls relevant to restring without
 * modifying the original [baseResources].
 */
internal class ResourcesDelegate(
        private val context: Context,
        private val baseResources: Resources,
        private val stringRepository: StringRepository
) {

    private var res: Resources = baseResources

    fun getIdentifier(name: String, defType: String?, defPackage: String?): Int {

        val identifier = baseResources.getIdentifier(name, defType, defPackage)

        if (defType == "string" && identifier == 0) {
            stringRepository.strings[Restring.locale]?.get(name) ?: return 0
            val stringId = UUID.randomUUID().hashCode()

            val managedString = Restring.managedStrings
                    .toList()
                    .firstOrNull { it.second == name }

            if (managedString != null) {
                return managedString.first
            }

            Restring.managedStrings[stringId] = name
            return stringId
        }

        return identifier
    }

    fun getString(id: Int): String {
        setLocale()

        val value = getStringFromRepository(id)
        return value?.toString() ?: res.getString(id)
    }

    fun getString(id: Int, vararg formatArgs: Any): String {
        setLocale()

        val value = getStringFromRepository(id)
        return try {
            if (value != null) String.format(value.toString(), *formatArgs)
            res.getString(id, *formatArgs)
        } catch (e: Exception) {
            runCatching { res.getString(id, *formatArgs) }.getOrDefault("")
        }
    }

    fun getText(id: Int): CharSequence {
        setLocale()

        val value = getStringFromRepository(id)
        return runCatching { value ?: res.getText(id) }.getOrDefault("")
    }

    fun getText(id: Int, def: CharSequence): CharSequence {
        setLocale()

        val value = runCatching {
            getStringFromRepository(id)
        }.getOrNull()

        return runCatching { value ?: res.getText(id, def) }.getOrDefault("")
    }

    fun getQuantityText(id: Int, quantity: Int): CharSequence {
        setLocale()

        val value = getQuantityStringFromRepository(id, quantity)
        return runCatching {
            value ?: res.getQuantityText(id, quantity)
        }.getOrDefault("")
    }

    fun getQuantityString(id: Int, quantity: Int): String {
        setLocale()

        val value = getQuantityStringFromRepository(id, quantity)?.toString()
        return runCatching {
            value ?: res.getQuantityString(id, quantity)
        }.getOrDefault("")
    }

    fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any?): String {
        setLocale()

        val value = getQuantityStringFromRepository(id, quantity)?.toString()
                ?.let {
                    try {
                        String.format(it, *formatArgs)
                    } catch (e: Exception) {
                        res.getQuantityString(id, quantity, *formatArgs)
                    }
                }
        return runCatching {
            value ?: res.getQuantityString(id, quantity, *formatArgs)
        }.getOrDefault("")

    }

    fun getStringArray(id: Int): Array<String> {
        setLocale()

        val value = getStringArrayFromRepository(id)
        return value?.map { it.toString() }?.toTypedArray() ?: res.getStringArray(id)
    }

    fun getTextArray(id: Int): Array<CharSequence> {
        setLocale()

        val value = getStringArrayFromRepository(id)
        return value ?: res.getTextArray(id)
    }

    private fun getQuantityStringFromRepository(id: Int, quantity: Int): CharSequence? {

        val resultLocale = getLocale() ?: return null

        val stringKey = baseResources.getResourceEntryName(id)
        val quantityString = stringRepository.quantityStrings[resultLocale]?.get(stringKey)
        return quantityString?.get(quantity.toPluralKeyword(resultLocale))
    }

    private fun getStringArrayFromRepository(id: Int): Array<CharSequence>? {
        val resultLocale = getLocale() ?: return null

        val stringKey = baseResources.getResourceEntryName(id)
        return stringRepository.stringArrays[resultLocale]?.get(stringKey)
    }

    private fun getStringFromRepository(id: Int): CharSequence? {

        val resultLocale = getLocale() ?: return null

        try {
            val stringKey = baseResources.getResourceEntryName(id)
            return stringRepository.strings[resultLocale]?.get(stringKey)
        } catch (e: Resources.NotFoundException) {

            val stringKey = Restring.managedStrings[id]
            if (stringKey != null) {
                return stringRepository.strings[resultLocale]?.get(stringKey)
            }

            throw e
        }
    }

    private fun getLocale(): Locale? {
        val currentLocale = Restring.locale
        val supportedLocales = stringRepository.supportedLocales

        return if (supportedLocales.contains(currentLocale)) {
            currentLocale
        } else {
            supportedLocales.find { it.language == currentLocale.language }
        }
    }

    @Suppress("DEPRECATION")
    private fun setLocale() {
        if (Restring.localeProvider.isInitial) return

        val conf = baseResources.configuration

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(Restring.locale)
            res = context.createConfigurationContext(conf).resources
        } else {
            conf.locale = Restring.locale
            res.updateConfiguration(conf, baseResources.displayMetrics)
        }
    }

    private fun Int.toPluralKeyword(locale: Locale): PluralKeyword =
            PluralKeyword.fromQuantity(baseResources, locale, this)
}