package com.hyperdesign.myapplication.presentation.menu.mvi

import com.hyperdesign.myapplication.domain.Entity.MenueResponse

data class MenuStateModel(
    val menuData : MenueResponse? = null,
    val isLoading : Boolean = false,
    val errorMessage : String? = null,
    val branchId : Int = 0
)
