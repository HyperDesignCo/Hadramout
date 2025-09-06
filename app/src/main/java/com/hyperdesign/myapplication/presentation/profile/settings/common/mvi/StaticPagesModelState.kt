package com.hyperdesign.myapplication.presentation.profile.settings.common.mvi

import com.hyperdesign.myapplication.domain.Entity.PagesResponseEntity

data class StaticPagesModelState(
    val isLoading : Boolean =false,
    val errorMsg :String?=null,
    val pagesResponse : PagesResponseEntity?=null
)
