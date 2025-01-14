package at.nukular.core.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun okHttpClient(
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder.build()
    }

    @Provides
    fun httpClient(): HttpClient = HttpClient {
        defaultRequest {
            host = "10.0.2.2:8080"
            url {
                protocol = URLProtocol.HTTP
            }
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }

        install(Logging) {
            logger = CustomAndroidHttpLogger
            level = LogLevel.ALL
        }
    }
}

/**
 * It's required since both [DEFAULT] and [ANDROID] uses SLF4J, which does nothing in Android by default
 * (prints warning about "no-operation (NOP) logger implementation").
 */
object CustomAndroidHttpLogger : Logger {
    private const val logTag = "CustomAndroidHttpLogger"

    override fun log(message: String) {
        Log.i(logTag, message)
    }
}