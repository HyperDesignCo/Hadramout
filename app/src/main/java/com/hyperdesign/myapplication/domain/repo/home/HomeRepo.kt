package com.hyperdesign.myapplication.domain.repo.home

import com.hyperdesign.myapplication.domain.Entity.AddressResponseEntity
import com.hyperdesign.myapplication.domain.Entity.BranchesResponse
import com.hyperdesign.myapplication.domain.Entity.HomeResponse

interface HomeRepo {

    suspend fun getBranches(): BranchesResponse

    suspend fun getHomeMenues(branchId: Int): HomeResponse

    suspend fun getAddress(): AddressResponseEntity
}