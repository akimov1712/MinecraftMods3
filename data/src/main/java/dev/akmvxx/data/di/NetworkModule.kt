package dev.akmvxx.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.akmvxx.data.BuildConfig
import dev.akmvxx.data.source.remote.bug.BugApi
import dev.akmvxx.data.source.remote.mod.ModApi
import dev.akmvxx.data.source.remote.propose.ProposeApi
import dev.akmvxx.data.source.remote.settings.SettingsApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor()
            .apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
        return OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideProposeApi(retrofit: Retrofit): ProposeApi =
        retrofit.create(ProposeApi::class.java)

    @Provides
    @Singleton
    fun provideBugApi(retrofit: Retrofit): BugApi =
        retrofit.create(BugApi::class.java)

    @Provides
    @Singleton
    fun provideModApi(retrofit: Retrofit): ModApi =
        retrofit.create(ModApi::class.java)


    @Provides
    @Singleton
    fun provideSettingsApi(retrofit: Retrofit): SettingsApi =
        retrofit.create(SettingsApi::class.java)

}
