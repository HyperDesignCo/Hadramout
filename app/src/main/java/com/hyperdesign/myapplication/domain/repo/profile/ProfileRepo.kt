package com.hyperdesign.myapplication.domain.repo.profile

import com.hyperdesign.myapplication.domain.Entity.AboutUsResponseEntity
import com.hyperdesign.myapplication.domain.Entity.AddToCartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.AddressEntity
import com.hyperdesign.myapplication.domain.Entity.AreaResponseEntity
import com.hyperdesign.myapplication.domain.Entity.CreateNewAddressRequest
import com.hyperdesign.myapplication.domain.Entity.DeleteAddressRequest
import com.hyperdesign.myapplication.domain.Entity.EditProfileRequest
import com.hyperdesign.myapplication.domain.Entity.EditProfileResponseEntity
import com.hyperdesign.myapplication.domain.Entity.OrdersResponseEntity
import com.hyperdesign.myapplication.domain.Entity.PagesResponseEntity
import com.hyperdesign.myapplication.domain.Entity.RegionResponseEntity
import com.hyperdesign.myapplication.domain.Entity.ReorderRequest
import com.hyperdesign.myapplication.domain.Entity.ShowAddressResponseEntity
import com.hyperdesign.myapplication.domain.Entity.updateAddressRequest

interface ProfileRepo {

    suspend fun showMyOrders(type:Int): OrdersResponseEntity
    suspend fun reOrder(reOrderRequest: ReorderRequest): AddToCartResponseEntity

    suspend fun displayAboutUS(): AboutUsResponseEntity

    suspend fun showPage(page: Int): PagesResponseEntity

    suspend fun getRegions(): RegionResponseEntity

    suspend fun getArea(regionId:Int): AreaResponseEntity

    suspend fun createNewAddress(createNewAddressRequest: CreateNewAddressRequest):AddToCartResponseEntity

    suspend fun deleteAddress(deleteAddressRequest: DeleteAddressRequest):AddToCartResponseEntity

    suspend fun showAddress(addressId:Int): ShowAddressResponseEntity

    suspend fun updateAddress(updateAddressRequest: updateAddressRequest):AddToCartResponseEntity

    suspend fun editProfile(editProfileRequest: EditProfileRequest): EditProfileResponseEntity

}