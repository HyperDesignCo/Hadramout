package com.hyperdesign.myapplication.domain.Entity

data class Branch(
    val id: Int,
    val title: String,
    val slug: String
)

// Optionally, if you need the response wrapper in the domain layer:
data class BranchesResponse(
    val branches: List<Branch>,
    val message: String
)