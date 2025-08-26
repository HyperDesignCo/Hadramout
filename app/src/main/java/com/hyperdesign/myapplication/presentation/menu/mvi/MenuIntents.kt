package com.hyperdesign.myapplication.presentation.menu.mvi

sealed class MenuIntents {
    data class getMenus(val branchId: Int) : MenuIntents()
    data class changeBranchId(val branchId: Int) : MenuIntents()

}