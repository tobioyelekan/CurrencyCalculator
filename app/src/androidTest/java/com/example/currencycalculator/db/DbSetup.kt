package com.example.currencycalculator.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.currencycalculator.data.local.CurrencyDatabase
import com.example.currencycalculator.data.local.dao.CurrencyRateDao
import com.example.currencycalculator.data.local.dao.CurrencySymbolDao
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class DbSetup {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: CurrencyDatabase
    protected lateinit var symbolDao: CurrencySymbolDao
    protected lateinit var rateDao: CurrencyRateDao

    @Before
    fun init() {
        db = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            CurrencyDatabase::class.java
        ).build()

        symbolDao = db.currencySymbolDao()
        rateDao = db.currencyRateDao()
    }

    @After
    fun closeDb() {
        db.close()
    }
}