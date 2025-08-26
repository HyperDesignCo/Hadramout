package com.hyperdesign.myapplication.presentation.di


import com.hyperdesign.myapplication.domain.usecase.auth.LoginUseCase
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginViewModel
import com.hyperdesign.myapplication.presentation.auth.signup.mvi.RegisterViewModel
import com.hyperdesign.myapplication.presentation.home.mvi.HomeViewModel
import com.hyperdesign.myapplication.presentation.menu.mvi.MenuViewModel
import com.hyperdesign.myapplication.presentation.utilies.ValidatePhoneNumber
import com.hyperdesign.myapplication.presentation.utilies.ValidateText
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModels = module {
    single<ValidateText> { ValidateText(get ()) }

    single<ValidatePhoneNumber> {
        ValidatePhoneNumber(get ())

    }


    viewModel<LoginViewModel> { LoginViewModel(get(), get(),get(),get()) }

    viewModel<RegisterViewModel>{
        RegisterViewModel(get(),get(),get(),get(),get())
    }

    viewModel<HomeViewModel> {
        HomeViewModel(get(),get())
    }

    viewModel<MenuViewModel> { MenuViewModel(get()) }
}


