package com.example.currencycalculator.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.currencycalculator.data.local.dao.CurrencyRateDao
import com.example.currencycalculator.data.local.dao.CurrencySymbolDao
import com.example.currencycalculator.data.model.CurrencyRate
import com.example.currencycalculator.data.model.CurrencySymbol

@Database(
    entities = [CurrencyRate::class, CurrencySymbol::class],
    version = 1,
    exportSchema = false
)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract fun currencyRateDao(): CurrencyRateDao
    abstract fun currencySymbolDao(): CurrencySymbolDao

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