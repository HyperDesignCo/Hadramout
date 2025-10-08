package com.hyperdesign.myapplication.presentation.di


import com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi.CreatePasswordViewModel
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi.ForgetPasswordViewModel
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi.VerifyViewModel
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginViewModel
import com.hyperdesign.myapplication.presentation.auth.signup.mvi.RegisterViewModel
import com.hyperdesign.myapplication.presentation.home.mvi.HomeViewModel
import com.hyperdesign.myapplication.presentation.main.mvi.AuthViewModel
import com.hyperdesign.myapplication.presentation.menu.mvi.CartViewModel
import com.hyperdesign.myapplication.presentation.menu.mvi.CheckOutViewModel
import com.hyperdesign.myapplication.presentation.menu.mvi.MealDetailsViewModel
import com.hyperdesign.myapplication.presentation.menu.mvi.MenuViewModel
import com.hyperdesign.myapplication.presentation.profile.addresses.mvi.AddressesViewModel
import com.hyperdesign.myapplication.presentation.profile.addresses.mvi.UpdateAddressViewModel
import com.hyperdesign.myapplication.presentation.profile.myorders.mvi.MyOrderViewModel
import com.hyperdesign.myapplication.presentation.profile.settings.common.mvi.SettingViewModel
import com.hyperdesign.myapplication.presentation.profile.settings.common.mvi.StaticPagesViewModel
import com.hyperdesign.myapplication.presentation.profile.settings.whoarewe.mvi.WhoAreWeViewModel
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
        HomeViewModel(get(),get(),get())
    }

    viewModel<MenuViewModel> { MenuViewModel(get(),get()) }

    viewModel<MealDetailsViewModel> {
        MealDetailsViewModel(get(),get(),get())
    }

    viewModel<CartViewModel>{
        CartViewModel(get(),get(),get(),get(),get())
    }

    viewModel<ForgetPasswordViewModel>{
        ForgetPasswordViewModel(get(),get(),get())
    }

    viewModel<AuthViewModel>{
        AuthViewModel(get(),get())
    }

    viewModel<VerifyViewModel>{
        VerifyViewModel()
    }

    viewModel<CreatePasswordViewModel>{
        CreatePasswordViewModel(get(),get(),get())
    }

    viewModel<CheckOutViewModel> {
        CheckOutViewModel(get(),get(),get())
    }

    viewModel<MyOrderViewModel>{
        MyOrderViewModel(get(),get())
    }

    viewModel<WhoAreWeViewModel>{
        WhoAreWeViewModel(get())
    }

    viewModel<SettingViewModel>{
        SettingViewModel(get(),get(),get())
    }

    viewModel<StaticPagesViewModel>{
        StaticPagesViewModel(get())
    }

    viewModel<AddressesViewModel>{
        AddressesViewModel(get(),get(),get(),get(),get(),get(),get(),get())
    }

    viewModel<UpdateAddressViewModel>{
        UpdateAddressViewModel(get(),get(),get(),get(),get())
    }
}



