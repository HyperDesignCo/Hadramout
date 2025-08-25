package com.hyperdesign.myapplication.presentation.home.mvi

sealed class HomeIntents {

    object GetBranches : HomeIntents()
    data class GetHomeMenuId(val id: Int) : HomeIntents()
    data class changeBranchId(val id: Int) : HomeIntents()


}