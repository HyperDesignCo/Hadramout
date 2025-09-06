package com.hyperdesign.myapplication.domain.usecase.profile

import com.hyperdesign.myapplication.domain.repo.profile.LanguageRepo

class GetLagugaueUsecase(

    private val languageRepo: LanguageRepo
) {

    operator fun invoke(): String = languageRepo.getLang()

}