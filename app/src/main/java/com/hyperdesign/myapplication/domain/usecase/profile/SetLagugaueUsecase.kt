package com.hyperdesign.myapplication.domain.usecase.profile

import com.hyperdesign.myapplication.domain.repo.profile.LanguageRepo

class SetLagugaueUsecase(
    private val languageRepo: LanguageRepo
) {

    suspend operator fun invoke(languge:String)=languageRepo.saveLang(languge)

}