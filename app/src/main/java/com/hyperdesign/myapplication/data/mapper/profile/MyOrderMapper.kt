package com.hyperdesign.myapplication.data.mapper.profile

import com.hyperdesign.myapplication.data.dto.AddressOrderDTO
import com.hyperdesign.myapplication.data.dto.AreaOrderDTO
import com.hyperdesign.myapplication.data.dto.BranchOrderDTO
import com.hyperdesign.myapplication.data.dto.MealOrderDTO
import com.hyperdesign.myapplication.data.dto.OrderDTO
import com.hyperdesign.myapplication.data.dto.OrdersResponseDTO
import com.hyperdesign.myapplication.data.dto.PaymentMethodOrderDTO
import com.hyperdesign.myapplication.data.dto.RegionOrderDTO
import com.hyperdesign.myapplication.data.dto.UserDTO
import com.hyperdesign.myapplication.domain.Entity.AddressOrderEntity
import com.hyperdesign.myapplication.domain.Entity.AreaOrderEntity
import com.hyperdesign.myapplication.domain.Entity.BranchOrderEntity
import com.hyperdesign.myapplication.domain.Entity.MealOrderEntity
import com.hyperdesign.myapplication.domain.Entity.OrderEntity
import com.hyperdesign.myapplication.domain.Entity.OrdersResponseEntity
import com.hyperdesign.myapplication.domain.Entity.PaymentMethodOrderEntity
import com.hyperdesign.myapplication.domain.Entity.RegionOrderEntity
import com.hyperdesign.myapplication.domain.Entity.UserOrderEntity

fun OrdersResponseDTO?.toEntity(): OrdersResponseEntity {
    return OrdersResponseEntity(
        orders = this?.orders?.map { it.toEntity() } ?: emptyList(),
        message = this?.message
    )
}

fun OrderDTO?.toEntity(): OrderEntity {
    return OrderEntity(
        id = this?.id,
        branch = this?.branch?.toEntity(),
        paymentMethod = this?.paymentMethod?.toEntity(),
        address = this?.address?.toEntity(),
        distance = this?.distance,
        status = this?.status,
        statusCode = this?.statusCode,
        paymentStatus = this?.paymentStatus,
        user = this?.user?.toEntity(),
        referenceNumber = this?.referenceNumber,
        primaryPrice = this?.primaryPrice,
        serviceChargeCost = this?.serviceChargeCost,
        vatCost = this?.vatCost,
        deliveryCost = this?.deliveryCost,
        coupon = this?.coupon,
        couponCost = this?.couponCost,
        offerDiscount = this?.offerDiscount,
        netPrice = this?.netPrice,
        createdAt = this?.createdAt,
        meals = this?.meals?.map { it.toEntity() } ?: emptyList()
    )
}

fun BranchOrderDTO?.toEntity(): BranchOrderEntity {
    return BranchOrderEntity(
        id = this?.id,
        title = this?.title,
        lat = this?.lat,
        lng = this?.lng
    )
}

fun PaymentMethodOrderDTO?.toEntity(): PaymentMethodOrderEntity? {
    return this?.let {
        PaymentMethodOrderEntity(
            id = it.id,
            title = it.title
        )
    }
}

fun AddressOrderDTO?.toEntity(): AddressOrderEntity? {
    return this?.let {
        AddressOrderEntity(
            id = it.id,
            region = it.region?.toEntity(),
            area = it.area?.toEntity(),
            subRegion = it.subRegion,
            street = it.street,
            specialSign = it.specialSign,
            buildingNumber = it.buildingNumber,
            floorNumber = it.floorNumber,
            mainPhone = it.mainPhone,
            phone = it.phone,
            apartmentNumber = it.apartmentNumber,
            extraInfo = it.extraInfo,
            latitude = it.latitude,
            longitude = it.longitude
        )
    }
}

fun RegionOrderDTO?.toEntity(): RegionOrderEntity? {
    return this?.let {
        RegionOrderEntity(
            id = it.id,
            name = it.name
        )
    }
}

fun AreaOrderDTO?.toEntity(): AreaOrderEntity? {
    return this?.let {
        AreaOrderEntity(
            id = it.id,
            name = it.name
        )
    }
}

fun UserDTO?.toEntity(): UserOrderEntity? {
    return this?.let {
        UserOrderEntity(
            id = it.id,
            name = it.name
        )
    }
}

fun MealOrderDTO?.toEntity(): MealOrderEntity {
    return MealOrderEntity(
        id = this?.id,
        mealTitle = this?.mealTitle,
        mealImage = this?.mealImage,
        sizeTitle = this?.sizeTitle,
        price = this?.price,
        quantity = this?.quantity,
        subChoicesPrice = this?.subChoicesPrice,
        primaryPrice = this?.primaryPrice,
        totalPrice = this?.totalPrice,
        comment = this?.comment,
        choices = this?.choices ?: emptyList()
    )
}