package com.example.emailtosms.data.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.emailtosms.data.mapper.MapperEmailToSms
import com.example.emailtosms.domain.email.GetEmailListWithTokenUseCase
import com.example.emailtosms.domain.sms.AddSmsItemUseCase
import javax.inject.Inject

class RefreshEmailWorkerFactory @Inject constructor(
            private val addSmsItemUseCase: AddSmsItemUseCase,
            private val getEmailListWithTokenUseCase: GetEmailListWithTokenUseCase,
            private val mapperEmailToSms: MapperEmailToSms
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return RefreshEmailWorker(
            appContext,
            workerParameters,
            addSmsItemUseCase,
            getEmailListWithTokenUseCase,
            mapperEmailToSms
        )
    }
}