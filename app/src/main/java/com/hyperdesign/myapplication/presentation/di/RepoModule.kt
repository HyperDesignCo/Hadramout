package com.hyperdesign.myapplication.presentation.di

import com.hyperdesign.myapplication.data.remote.common.FirebaseRepository
import com.hyperdesign.myapplication.data.repo.AuthRepo.AuthRepoImpl
import com.hyperdesign.myapplication.data.repo.homerepo.HomeRepoImpl
import com.hyperdesign.myapplication.data.repo.menurepo.MenuRepoImpl
import com.hyperdesign.myapplication.data.repo.profilerepo.LanguageRepoImpl
import com.hyperdesign.myapplication.data.repo.profilerepo.ProfileRepoImpl
import com.hyperdesign.myapplication.domain.repo.auth.AuthRepo
import com.hyperdesign.myapplication.domain.repo.home.HomeRepo
import com.hyperdesign.myapplication.domain.repo.menu.MenuRepo
import com.hyperdesign.myapplication.domain.repo.profile.LanguageRepo
import com.hyperdesign.myapplication.domain.repo.profile.ProfileRepo
import org.koin.dsl.module

val RepoModule = module {

    single<AuthRepo> {
        AuthRepoImpl(get ())


    }


    single<HomeRepo> {
        HomeRepoImpl(get())
    }

    single<MenuRepo> {
        MenuRepoImpl(get())
    }

    single<ProfileRepo> {
        ProfileRepoImpl(get())
    }

    single<LanguageRepo> {
        LanguageRepoImpl(get())
    }

    single<FirebaseRepository> {
        FirebaseRepository()
    }






}