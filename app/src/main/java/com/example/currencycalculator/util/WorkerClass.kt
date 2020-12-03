package com.example.currencycalculator.util

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.currencycalculator.data.helper.Resource.Status
import com.example.currencycalculator.data.source.repo.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class WorkerClass @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val currencyRepo: CurrencyRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result =
        withContext(Dispatchers.IO) {
            try {
                val response = currencyRepo.callNetwork()
                when (response.status) {
                    Status.SUCCESS -> Result.success()
                    else -> Result.failure()
                }
            } catch (e: IOException) {
                Result.failure()
            }
        }
}