package com.hyperdesign.myapplication.presentation.profile.points.ui.viewmodel

import com.hyperdesign.myapplication.domain.Entity.GetProfileEntity

data class PointsState(
    val isLoading: Boolean = false,
    val errorMsg: String = "",
    val profileData: GetProfileEntity? = null
)
