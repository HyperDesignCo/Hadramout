package com.hyperdesign.myapplication.data.dto

import com.google.gson.annotations.SerializedName

data class BranchesResponseDTO(
    val branches: List<BranchDTO>,
    val message: String
)

data class BranchDTO(
    val id: Int,
    val title: String,
    val slug: String,
    @SerializedName("open_time")
    val openTime: String,
    @SerializedName("close_time")
    val closeTime: String
)