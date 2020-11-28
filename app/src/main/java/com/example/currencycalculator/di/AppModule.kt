package com.example.currencycalculator.di

import android.content.Context
import com.example.currencycalculator.*
import com.example.currencycalculator.BuildConfig.BASE_URL
import com.example.currencycalculator.data.local.dao.CurrencyRateDao
import com.example.currencycalculator.data.local.CurrencyDatabase
import com.example.currencycalculator.data.local.dao.CurrencySymbolDao
import com.example.currencycalculator.data.remote.CurrencyConverterService
import com.example.currencycalculator.data.remote.TokenInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun providesRateDao(@ApplicationContext context: Context): CurrencyRateDao {
        return CurrencyDatabase.getDatabase(context).currencyRateDao()
    }

    @Provides
    @Singleton
    fun provideSymbolDao(@ApplicationContext context: Context): CurrencySymbolDao {
        return CurrencyDatabase.getDatabase(context).currencySymbolDao()
    }

    @Provides
    @Singleton
    fun providesRetrofit(tokenInterceptor: TokenInterceptor): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor)
        }

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .client(builder.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideCurrencyRateService(retrofit: Retrofit): CurrencyConverterService =
        retrofit.create(CurrencyConverterService::class.java)
}