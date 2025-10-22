package com.hyperdesign.myapplication.data.remote.home

import com.hyperdesign.myapplication.data.dto.AddressResponseDto
import com.hyperdesign.myapplication.data.dto.BranchesResponseDTO
import com.hyperdesign.myapplication.data.dto.CheckLocationResponseDto
import com.hyperdesign.myapplication.data.dto.HomeResponseDTO
import com.hyperdesign.myapplication.domain.Entity.checkLocationRequest

interface HomeApiServices {

    suspend fun getBranches(): BranchesResponseDTO

    suspend fun getHomeMenues(branchId: Int): HomeResponseDTO

    suspend fun getAddress(): AddressResponseDto

    suspend fun checkLocation(checkLocationRequest: checkLocationRequest): CheckLocationResponseDto



}