package com.hyperdesign.myapplication.data.dto


data class PagesResponseDto(
    val pages: PagesDTO
)

data class PagesDTO(
    val id: Int,
    val title: String,
    val image: String,
    val text: String
)