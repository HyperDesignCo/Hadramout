package com.hyperdesign.myapplication.data.remote.menu

import com.hyperdesign.myapplication.data.dto.MenuResponseDto

interface MenuApiServices {
    suspend fun getMenus(branchId: Int): MenuResponseDto
}