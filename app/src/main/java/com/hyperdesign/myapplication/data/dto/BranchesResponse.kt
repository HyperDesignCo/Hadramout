package com.hyperdesign.myapplication.data.dto

data class BranchesResponseDTO(
    val branches: List<BranchDTO>,
    val message: String
)

data class BranchDTO(
    val id: Int,
    val title: String,
    val slug: String
)