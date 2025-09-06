package com.hyperdesign.myapplication.data.repo.profilerepo

import com.hyperdesign.myapplication.data.local.TokenManager
import com.hyperdesign.myapplication.domain.repo.profile.LanguageRepo

class LanguageRepoImpl(
    private val tokenManager: TokenManager
): LanguageRepo {
    override fun saveLang(lang: String) {
        tokenManager.saveLanguage(lang)
    }

    override fun getLang(): String {
        return tokenManager.getLanguage() ?: "en"

    }

}