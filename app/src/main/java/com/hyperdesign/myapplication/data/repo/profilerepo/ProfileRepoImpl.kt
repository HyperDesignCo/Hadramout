package com.hyperdesign.myapplication.data.repo.profilerepo

import com.hyperdesign.myapplication.data.mapper.home.toDomain
import com.hyperdesign.myapplication.data.mapper.menu.toDomain
import com.hyperdesign.myapplication.data.mapper.profile.setting.toDomain
import com.hyperdesign.myapplication.data.mapper.profile.setting.toEntity
import com.hyperdesign.myapplication.data.mapper.profile.toDomain
import com.hyperdesign.myapplication.data.mapper.profile.toEntity
import com.hyperdesign.myapplication.data.remote.profile.ProfileApiServices
import com.hyperdesign.myapplication.domain.Entity.AboutUsResponseEntity
import com.hyperdesign.myapplication.domain.Entity.AddToCartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.AddressEntity
import com.hyperdesign.myapplication.domain.Entity.AreaResponseEntity
import com.hyperdesign.myapplication.domain.Entity.CreateNewAddressRequest
import com.hyperdesign.myapplication.domain.Entity.DeleteAddressRequest
import com.hyperdesign.myapplication.domain.Entity.OrdersResponseEntity
import com.hyperdesign.myapplication.domain.Entity.PagesResponseEntity
import com.hyperdesign.myapplication.domain.Entity.RegionResponseEntity
import com.hyperdesign.myapplication.domain.Entity.ReorderRequest
import com.hyperdesign.myapplication.domain.Entity.ShowAddressResponseEntity
import com.hyperdesign.myapplication.domain.Entity.updateAddressRequest
import com.hyperdesign.myapplication.domain.repo.profile.ProfileRepo

class ProfileRepoImpl(
    private val profileApiServices: ProfileApiServices
): ProfileRepo {
    override suspend fun showMyOrders(type: Int): OrdersResponseEntity {
        val response = profileApiServices.showMyOrders(type)
        return response.toEntity()
    }

    override suspend fun reOrder(reOrderRequest: ReorderRequest): AddToCartResponseEntity {
        val response = profileApiServices.reOrder(reOrderRequest)
        return response.toDomain()
    }

    override suspend fun displayAboutUS(): AboutUsResponseEntity {
        val response = profileApiServices.displayAbouUS()
        return response.toEntity()
    }

    override suspend fun showPage(page: Int): PagesResponseEntity {
        val response =profileApiServices.showPage(page)
        return response.toDomain()
    }

    override suspend fun getRegions(): RegionResponseEntity {
        val response = profileApiServices.getRegions()
        return response.toDomain()
    }

    override suspend fun getArea(regionId: Int): AreaResponseEntity {
        val response = profileApiServices.getArea(regionId)
        return response.toDomain()
    }

    override suspend fun createNewAddress(createNewAddressRequest: CreateNewAddressRequest): AddToCartResponseEntity {
        val response = profileApiServices.createNewAddress(createNewAddressRequest)
         return response.toDomain()
    }

    override suspend fun deleteAddress(deleteAddressRequest: DeleteAddressRequest): AddToCartResponseEntity {
        val response = profileApiServices.deleteAddress(deleteAddressRequest)
        return response.toDomain()
    }

    override suspend fun showAddress(addressId: Int): ShowAddressResponseEntity {
        val response = profileApiServices.showAddress(addressId)
        return response.toDomain()
    }

    override suspend fun updateAddress(updateAddressRequest: updateAddressRequest): AddToCartResponseEntity {
        val response = profileApiServices.updateAddress(updateAddressRequest)
        return response.toDomain()
    }
}