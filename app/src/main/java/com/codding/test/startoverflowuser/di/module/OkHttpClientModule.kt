package com.codding.test.startoverflowuser.di.module

import android.content.Context
import com.codding.test.startoverflowuser.di.qualifier.ApplicationContext
import com.codding.test.startoverflowuser.util.CacheConstant
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File

@Module(includes = [AppModule::class])
class OkHttpClientModule {

    @Provides
    fun okHttpClient(cache: Cache, httpLoggingInterceptor: HttpLoggingInterceptor) : OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .cache(cache)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun cache(file : File) : Cache {
        return Cache(file, CacheConstant.CACHE_SIZE_IN_MB * 1000 * 1000)
    }

    @Provides
    fun file(@ApplicationContext context: Context) : File {
        var file = File(context.cacheDir, CacheConstant.CACHE_FOLDER_NAME)
        file.mkdir()
        return file
    }

    @Provides
    fun httpLoggingInterceptor() : HttpLoggingInterceptor {
        var logger = HttpLoggingInterceptor { message -> Timber.d(message)  }
        logger.level = HttpLoggingInterceptor.Level.BODY
        return logger
    }
}