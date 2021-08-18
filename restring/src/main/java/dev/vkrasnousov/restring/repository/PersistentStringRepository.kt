package dev.vkrasnousov.restring.repository

import dev.vkrasnousov.restring.MutableStringRepository
import dev.vkrasnousov.restring.PluralKeyword
import dev.vkrasnousov.restring.internal.repository.persistent.LocalesPersistentSet
import dev.vkrasnousov.restring.internal.repository.persistent.LocalizedResourcesPersistentMap
import dev.vkrasnousov.restring.internal.repository.persistent.ResourcesPersistentMap
import java.util.*

class PersistentStringRepository(
        localesValueSetStore: ValueSetStore<Locale>,
        stringsKeyValueStoreFactory: (locale: Locale) -> KeyValueStore<String, CharSequence>,
        quantityStringsKeyValueStoreFactory: (locale: Locale) -> KeyValueStore<String, Map<PluralKeyword, CharSequence>>,
        stringArraysKeyValueStoreFactory: (locale: Locale) -> KeyValueStore<String, Array<CharSequence>>,
) : MutableStringRepository {

    override val supportedLocales: Set<Locale>
        get() = _supportedLocales

    private val _supportedLocales by LocalesPersistentSet(localesValueSetStore)

    override val strings by localizedResourcesPersistentMap(stringsKeyValueStoreFactory)

    override val quantityStrings by localizedResourcesPersistentMap(quantityStringsKeyValueStoreFactory)

    override val stringArrays by localizedResourcesPersistentMap(stringArraysKeyValueStoreFactory)

    private inline fun <V> localizedResourcesPersistentMap(
            crossinline keyValueStoreFactory: (locale: Locale) -> KeyValueStore<String, V>
    ): LocalizedResourcesPersistentMap<V> {
        return LocalizedResourcesPersistentMap(
                locales = _supportedLocales,
                persistentMapFactory = { locale ->
                    ResourcesPersistentMap(
                            keyValueStoreFactory.invoke(locale)
                    )
                }
        )
    }
}