package com.hyperdesign.myapplication.presentation.di

import com.hyperdesign.myapplication.data.local.TokenManager
import org.koin.dsl.module

val localDataModule = module {
    single { TokenManager(get()) }
}