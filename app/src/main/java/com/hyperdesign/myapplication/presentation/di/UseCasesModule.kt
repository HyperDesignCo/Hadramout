package com.hyperdesign.myapplication.presentation.di

import com.hyperdesign.myapplication.domain.usecase.auth.CreateNewPasswordUseCase
import com.hyperdesign.myapplication.domain.usecase.auth.ForgetPasswordUseCase
import com.hyperdesign.myapplication.domain.usecase.auth.LoginUseCase
import com.hyperdesign.myapplication.domain.usecase.auth.RefreshTokenUseCase
import com.hyperdesign.myapplication.domain.usecase.auth.RegisterUseCase
import com.hyperdesign.myapplication.domain.usecase.cart.CheckCouponUseCase
import com.hyperdesign.myapplication.domain.usecase.cart.CheckOutUseCase
import com.hyperdesign.myapplication.domain.usecase.cart.DeleteCartItemUseCase
import com.hyperdesign.myapplication.domain.usecase.cart.FinishOrderUseCase
import com.hyperdesign.myapplication.domain.usecase.cart.ShowCartUseCase
import com.hyperdesign.myapplication.domain.usecase.cart.UpdateCartItemQuantityUseCase
import com.hyperdesign.myapplication.domain.usecase.home.CheckLocationUseCase
import com.hyperdesign.myapplication.domain.usecase.home.GetAllAddressUseCase
import com.hyperdesign.myapplication.domain.usecase.home.GetBranchesUseCase
import com.hyperdesign.myapplication.domain.usecase.home.GetHomeMenueUseCase
import com.hyperdesign.myapplication.domain.usecase.menu.AddMealToCartUseCase
import com.hyperdesign.myapplication.domain.usecase.menu.GetMealDetailsUseCase
import com.hyperdesign.myapplication.domain.usecase.menu.GetMenuUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.CreateNewAddressUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.DeleteAddressUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.DisplayAboutUsUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.EditProfileUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.GetAreaUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.GetLagugaueUsecase
import com.hyperdesign.myapplication.domain.usecase.profile.GetRegionsUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.ReOrderUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.SetLagugaueUsecase
import com.hyperdesign.myapplication.domain.usecase.profile.ShowAddressUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.ShowMyOrdersUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.ShowStaticPageUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.UpdateAddressUseCase
import io.ktor.http.auth.HttpAuthHeader
import org.koin.dsl.module


val useCasesModule = module {

    single<LoginUseCase> {
        LoginUseCase(get())
    }

    single<RegisterUseCase> {
        RegisterUseCase(get())
    }

    single<GetBranchesUseCase> {
        GetBranchesUseCase(get())
    }

    single<GetHomeMenueUseCase> {
        GetHomeMenueUseCase(get())
    }

    single<GetMenuUseCase> {
        GetMenuUseCase(get())
    }

    single<GetMealDetailsUseCase> {
        GetMealDetailsUseCase(get())
    }

    single<AddMealToCartUseCase> {
        AddMealToCartUseCase(get())
    }

    single<ShowCartUseCase> {
        ShowCartUseCase(get())
    }

    single<ForgetPasswordUseCase>{
        ForgetPasswordUseCase(get())
    }

    single<RefreshTokenUseCase> {
        RefreshTokenUseCase(get())
    }

    single<CreateNewPasswordUseCase> {
        CreateNewPasswordUseCase(get())
    }

    single<DeleteCartItemUseCase> {
        DeleteCartItemUseCase(get())
    }
    single<UpdateCartItemQuantityUseCase> {
        UpdateCartItemQuantityUseCase(get())
    }

    single<CheckCouponUseCase> {
        CheckCouponUseCase(get())
    }

    single<GetAllAddressUseCase> {
        GetAllAddressUseCase(get())
    }

    single<CheckOutUseCase> {
        CheckOutUseCase(get())
    }

    single<FinishOrderUseCase> {
        FinishOrderUseCase(get())
    }

    single<ReOrderUseCase> {
        ReOrderUseCase(get())
    }

    single<ShowMyOrdersUseCase> {
        ShowMyOrdersUseCase(get())
    }


    single<DisplayAboutUsUseCase> {
        DisplayAboutUsUseCase(get())
    }

    single<SetLagugaueUsecase> {
        SetLagugaueUsecase(get())
    }

    single<GetLagugaueUsecase> {
        GetLagugaueUsecase(get())
    }

    single<ShowStaticPageUseCase> {
        ShowStaticPageUseCase(get())
    }

    single<GetRegionsUseCase> {
        GetRegionsUseCase(get())
    }

    single<GetAreaUseCase> {
        GetAreaUseCase(get())
    }

    single<CreateNewAddressUseCase> {
        CreateNewAddressUseCase(get())
    }

    single<DeleteAddressUseCase> {
        DeleteAddressUseCase(get())
    }

    single<ShowAddressUseCase> {
        ShowAddressUseCase(get())
    }

    single<UpdateAddressUseCase> {
        UpdateAddressUseCase(get())
    }

    single<EditProfileUseCase> {
        EditProfileUseCase(get())
    }
    single<CheckLocationUseCase> {
        CheckLocationUseCase(get())
    }

    }