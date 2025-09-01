package com.hyperdesign.myapplication.data.remote.home

import com.hyperdesign.myapplication.data.dto.AddressResponseDto
import com.hyperdesign.myapplication.data.dto.BranchesResponseDTO
import com.hyperdesign.myapplication.data.dto.HomeResponseDTO

interface HomeApiServices {

    suspend fun getBranches(): BranchesResponseDTO

    suspend fun getHomeMenues(branchId: Int): HomeResponseDTO

    suspend fun getAddress(): AddressResponseDto
}