package com.sukhralia.gameheist.di.module

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.sukhralia.gameheist.BuildConfig
import com.sukhralia.gameheist.network.GameHeistApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object GameHeistModule {

    @Singleton
    @Provides
    fun provideMoshiClient(): Moshi {

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        return moshi

    }

    @Singleton
    @Provides
    fun provideRetrofitClient(moshi: Moshi): Retrofit {

       val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .baseUrl(BuildConfig.APP_BASE_URL)
                .build()
        }

        return retrofit
    }

    @Singleton
    @Provides
    fun provideGameHeistApiService(retrofit : Retrofit) : GameHeistApiService {
        val service: GameHeistApiService by lazy {
            retrofit.create(GameHeistApiService::class.java)
        }
        return service
    }

}