package com.hyperdesign.myapplication.presentation.di

import com.hyperdesign.myapplication.domain.usecase.auth.LoginUseCase
import com.hyperdesign.myapplication.domain.usecase.auth.RegisterUseCase
import org.koin.dsl.module


val useCasesModule = module {

    single<LoginUseCase> {
        LoginUseCase(get())
    }

    single<RegisterUseCase> {
        RegisterUseCase(get())
    }
}