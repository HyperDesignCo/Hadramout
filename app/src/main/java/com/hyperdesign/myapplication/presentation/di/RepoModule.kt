package com.hyperdesign.myapplication.presentation.di

import com.hyperdesign.myapplication.data.repo.AuthRepo.AuthRepoImpl
import com.hyperdesign.myapplication.domain.repo.AuthRepo
import org.koin.dsl.module

val RepoModule = module {

    single<AuthRepo> {
        AuthRepoImpl(get ())


    }





}