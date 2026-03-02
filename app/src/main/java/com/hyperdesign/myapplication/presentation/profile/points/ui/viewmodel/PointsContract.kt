package com.hyperdesign.myapplication.presentation.profile.points.ui.viewmodel

interface PointsContract {
    sealed class Intent {
        object LoadProfile : Intent()
    }
}