package com.hyperdesign.myapplication.domain.repo.profile

interface LanguageRepo {
    fun saveLang(lang: String)
    fun getLang(): String
}