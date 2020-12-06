package com.example.currencycalculator.di

import android.content.Context
import com.example.currencycalculator.*
import com.example.currencycalculator.BuildConfig.BASE_URL
import com.example.currencycalculator.data.source.local.dao.RateDao
import com.example.currencycalculator.data.source.local.CurrencyDatabase
import com.example.currencycalculator.data.source.local.dao.SymbolDao
import com.example.currencycalculator.data.source.remote.CurrencyConverterService
import com.example.currencycalculator.data.helper.TokenInterceptor
import com.example.currencycalculator.data.source.local.CurrencyLocalDataSource
import com.example.currencycalculator.data.source.local.CurrencyLocalDataSourceImpl
import com.example.currencycalculator.data.source.remote.CurrencyRemoteDataSource
import com.example.currencycalculator.data.source.remote.CurrencyRemoteDataSourceImpl
import com.example.currencycalculator.data.source.repo.CurrencyRepository
import com.example.currencycalculator.data.source.repo.DefaultCurrencyRepository
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
    fun providesRateDao(@ApplicationContext context: Context): RateDao {
        return CurrencyDatabase.getDatabase(context).currencyRateDao()
    }

    @Provides
    @Singleton
    fun provideSymbolDao(@ApplicationContext context: Context): SymbolDao {
        return CurrencyDatabase.getDatabase(context).currencySymbolDao()
    }

    @Provides
    @Singleton
    fun provideRepository(
        remoteSource: CurrencyRemoteDataSource,
        localSource: CurrencyLocalDataSource,
        @ApplicationContext context: Context
    ): CurrencyRepository {
        return DefaultCurrencyRepository(remoteSource, localSource, context = context)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: CurrencyConverterService): CurrencyRemoteDataSource {
        return CurrencyRemoteDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(rateDao: RateDao, symbolDao: SymbolDao): CurrencyLocalDataSource {
        return CurrencyLocalDataSourceImpl(rateDao, symbolDao)
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