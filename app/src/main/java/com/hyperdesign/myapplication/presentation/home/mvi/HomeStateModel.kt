package com.hyperdesign.myapplication.presentation.home.mvi

import com.hyperdesign.myapplication.domain.Entity.BranchesResponse
import com.hyperdesign.myapplication.domain.Entity.HomeResponse

data class HomeStateModel(
    val branches: BranchesResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val homeMenues: HomeResponse? = null,
    val branchId : Int = 0
)
