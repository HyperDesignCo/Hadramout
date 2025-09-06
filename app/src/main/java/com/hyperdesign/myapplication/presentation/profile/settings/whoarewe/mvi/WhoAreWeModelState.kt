package com.hyperdesign.myapplication.presentation.profile.settings.whoarewe.mvi

import com.hyperdesign.myapplication.domain.Entity.AboutUsResponseEntity

data class WhoAreWeModelState(
    val isLoading: Boolean =false,
    val errorMsg :String?=null,
    val aboutUsResponse : AboutUsResponseEntity?=null
)
