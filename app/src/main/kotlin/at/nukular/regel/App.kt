package at.nukular.regel

import android.app.Application
import android.content.res.Configuration
import at.nukular.core.AppLifecycleTracker
import at.nukular.core.di.koin.appModule
import at.nukular.core.extensions.AppConfig
import at.nukular.regel.di.mapperModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class App : Application(),
    AppLifecycleTracker.AppStateCallback {

    @Inject
    lateinit var fileWriterReader: FileWriterReader


    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(AppLifecycleTracker(this))

        AppConfig.onConfigChanged(this, null)
        initKoin()
        initTimber()
    }

    private fun initKoin() {
        startKoin {
            // Reference Android context
            androidContext(this@App)
            modules(mapperModule, appModule)
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        AppConfig.onConfigChanged(this, newConfig)
    }

    // ========================================================================================================================
    // region AppLifecycleTracker

    override fun onAppMovedToForeground() {

    }

    override fun onAppMovedToBackground() {
        fileWriterReader.writeEntriesToFile()
    }
    // endregion
}