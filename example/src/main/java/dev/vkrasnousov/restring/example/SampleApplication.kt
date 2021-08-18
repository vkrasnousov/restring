package dev.vkrasnousov.restring.example

import android.app.Application
import dev.b3nedikt.app_locale.AppLocale
import dev.b3nedikt.reword.RewordInterceptor
import dev.b3nedikt.viewpump.ViewPump
import dev.vkrasnousov.restring.Restring
import dev.vkrasnousov.restring.example.Locales.LOCALE_AUSTRIAN_GERMAN
import java.util.*


class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppLocale.supportedLocales = listOf(Locale.ENGLISH, Locale.US, LOCALE_AUSTRIAN_GERMAN)

        Restring.init(this)
        Restring.localeProvider = AppLocaleLocaleProvider

        ViewPump.init(RewordInterceptor)
    }
}