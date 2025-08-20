package com.hyperdesign.myapplication.presentation.di

import com.hyperdesign.myapplication.presentation.utilies.ValidatePhoneNumber
import com.hyperdesign.myapplication.presentation.utilies.ValidateText
import org.koin.dsl.module

val utiliesModule = module{

    single<ValidateText> {
        ValidateText(get())
    }


    single<ValidatePhoneNumber> {
        ValidatePhoneNumber(get ())
    }
}