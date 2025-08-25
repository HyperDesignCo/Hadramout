package com.hyperdesign.myapplication.data.repo.homerepo

import com.hyperdesign.myapplication.data.mapper.home.toDomain
import com.hyperdesign.myapplication.data.remote.home.HomeApiServices
import com.hyperdesign.myapplication.domain.Entity.BranchesResponse
import com.hyperdesign.myapplication.domain.Entity.HomeResponse
import com.hyperdesign.myapplication.domain.repo.home.HomeRepo

class HomeRepoImpl(
    private val homeApiServices: HomeApiServices
) : HomeRepo {
    override suspend fun getBranches(): BranchesResponse {
        val response = homeApiServices.getBranches()
        return response.toDomain()
    }

    override suspend fun getHomeMenues(branchId: Int): HomeResponse {
        val response = homeApiServices.getHomeMenues(branchId)
        return response.toDomain()
    }
}