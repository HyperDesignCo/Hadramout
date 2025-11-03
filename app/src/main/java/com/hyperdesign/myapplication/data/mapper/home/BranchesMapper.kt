package com.hyperdesign.myapplication.data.mapper.home

import com.hyperdesign.myapplication.data.dto.BranchDTO
import com.hyperdesign.myapplication.data.dto.BranchesResponseDTO
import com.hyperdesign.myapplication.domain.Entity.Branch
import com.hyperdesign.myapplication.domain.Entity.BranchesResponse

fun BranchDTO.toDomain(): Branch {
    return Branch(
        id = id,
        title = title,
        slug = slug,
        openTime = openTime,
        closeTime = closeTime
    )
}


fun BranchesResponseDTO.toDomain(): BranchesResponse {
    return BranchesResponse(
        branches = branches.map { it.toDomain() },
        message = message
    )
}