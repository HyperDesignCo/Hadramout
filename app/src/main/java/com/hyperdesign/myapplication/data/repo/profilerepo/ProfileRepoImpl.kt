
package com.hyperdesign.myapplication.data.repo.profilerepo

import android.content.Context
import android.net.Uri
import android.util.Log
import com.hyperdesign.myapplication.data.mapper.home.toDomain
import com.hyperdesign.myapplication.data.mapper.menu.toDomain
import com.hyperdesign.myapplication.data.mapper.profile.setting.toDomain
import com.hyperdesign.myapplication.data.mapper.profile.setting.toEntity
import com.hyperdesign.myapplication.data.mapper.profile.toDomain
import com.hyperdesign.myapplication.data.mapper.profile.toEntity
import com.hyperdesign.myapplication.data.remote.profile.ProfileApiServices
import com.hyperdesign.myapplication.domain.Entity.*
import com.hyperdesign.myapplication.domain.repo.profile.ProfileRepo
import dagger.hilt.android.qualifiers.ApplicationContext

class ProfileRepoImpl(
    private val profileApiServices: ProfileApiServices,
    @ApplicationContext private val context: Context
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
        val response = profileApiServices.showPage(page)
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

    override suspend fun editProfile(editProfileRequest: EditProfileRequest): EditProfileResponseEntity {
        // Convert image URI to ByteArray if present
        val processedRequest = if (editProfileRequest.image != null && editProfileRequest.image.startsWith("content://")) {
            try {
                val inputStream = context.contentResolver.openInputStream(Uri.parse(editProfileRequest.image))
                val imageBytes = inputStream?.readBytes()
                inputStream?.close()

                Log.d("ProfileRepo", "Image read successfully, size: ${imageBytes?.size} bytes")

                // Create a new request with image bytes
                editProfileRequest.copy(
                    imageBytes = imageBytes
                )
            } catch (e: Exception) {
                Log.e("ProfileRepo", "Failed to read image: ${e.message}")
                editProfileRequest.copy(image = null) // Continue without image
            }
        } else {
            editProfileRequest
        }

        val response = profileApiServices.editProfile(processedRequest)

        // Validate response
        if (response.message.contains("failed", ignoreCase = true)) {
            throw Exception(response.message)
        }

        return response.toEntity()
    }
}