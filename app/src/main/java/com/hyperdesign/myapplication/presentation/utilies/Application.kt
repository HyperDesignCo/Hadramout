package com.hyperdesign.myapplication.presentation.utilies

import android.app.Application
import com.example.doctorona.data.di.networkingModule
import com.hyperdesign.myapplication.presentation.di.RepoModule
import com.hyperdesign.myapplication.presentation.di.localDataModule
import com.hyperdesign.myapplication.presentation.di.useCasesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize any global resources or configurations here
        // For example, you can initialize Koin or any other dependency injection framework here
         startKoin {
             androidContext(this@Application)
             modules(networkingModule,localDataModule,RepoModule, useCasesModule)
         }
    }
}