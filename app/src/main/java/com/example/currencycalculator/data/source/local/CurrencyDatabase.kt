package com.example.currencycalculator.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.currencycalculator.data.source.local.dao.RateDao
import com.example.currencycalculator.data.source.local.dao.SymbolDao
import com.example.currencycalculator.data.model.CurrencyRate
import com.example.currencycalculator.data.model.CurrencySymbol

@Database(
    entities = [CurrencyRate::class, CurrencySymbol::class],
    version = 1,
    exportSchema = false
)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract fun currencyRateDao(): RateDao
    abstract fun currencySymbolDao(): SymbolDao

    companion object {
        private var instance: CurrencyDatabase? = null

        fun getDatabase(context: Context): CurrencyDatabase {
            if (instance == null) {
                synchronized(CurrencyDatabase::class.java) {}
                instance =
                    Room.databaseBuilder(context, CurrencyDatabase::class.java, "AppDatabase")
                        .fallbackToDestructiveMigration()
                        .build()
            }

            return instance!!
        }
    }
}