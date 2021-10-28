package com.orangecoffeeapp

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*

@HiltAndroidApp
class MainApplication:LocalizationApplication() {

    override fun getDefaultLanguage(base: Context): Locale = Locale.getDefault()

}

