package com.example.currencycalculator.util

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.currencycalculator.repo.CurrencyConverterRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

class WorkerClass @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repo: CurrencyConverterRepo
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result =
        withContext(Dispatchers.IO) {
            try {
                when (repo.callNetwork()) {
                    "success" -> Result.success()
                    else -> Result.failure()
                }
            } catch (e: IOException) {
                Timber.e(e, e.message)
                Result.failure()
            }
        }
}