package com.hyperdesign.myapplication.domain.repo.menu

import com.hyperdesign.myapplication.domain.Entity.MenueResponse

interface MenuRepo {
    suspend fun getMenus(branchId: Int): MenueResponse
}