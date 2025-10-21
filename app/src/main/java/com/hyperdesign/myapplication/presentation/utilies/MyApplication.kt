package com.hyperdesign.myapplication.presentation.utilies

import android.app.Application
import com.example.doctorona.data.di.networkingModule
import com.google.android.libraries.places.api.Places
import com.hyperdesign.myapplication.BuildConfig
import com.hyperdesign.myapplication.presentation.di.RepoModule
import com.hyperdesign.myapplication.presentation.di.localDataModule
import com.hyperdesign.myapplication.presentation.di.useCasesModule
import com.hyperdesign.myapplication.presentation.di.utiliesModule
import com.hyperdesign.myapplication.presentation.di.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        // Initialize any global resources or configurations here
        // For example, you can initialize Koin or any other dependency injection framework here
         startKoin {
             androidContext(this@MyApplication)
             modules(networkingModule,localDataModule,RepoModule, useCasesModule,utiliesModule,viewModels)
         }
    }
}