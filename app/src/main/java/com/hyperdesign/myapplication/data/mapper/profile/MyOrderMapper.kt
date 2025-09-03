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


fun OrdersResponseDTO.toEntity(): OrdersResponseEntity {
    return OrdersResponseEntity(
        orders = this.orders.map { it.toEntity() },
        message = this.message
    )
}

fun OrderDTO.toEntity(): OrderEntity {
    return OrderEntity(
        id = this.id,
        branch = this.branch.toEntity(),
        paymentMethod = this.paymentMethod?.toEntity(),
        address = this.address?.toEntity(),
        distance = this.distance,
        status = this.status,
        statusCode = this.statusCode,
        paymentStatus = this.paymentStatus,
        user = this.user.toEntity(),
        referenceNumber = this.referenceNumber,
        primaryPrice = this.primaryPrice,
        serviceChargeCost = this.serviceChargeCost,
        vatCost = this.vatCost,
        deliveryCost = this.deliveryCost,
        coupon = this.coupon,
        couponCost = this.couponCost,
        offerDiscount = this.offerDiscount,
        netPrice = this.netPrice,
        createdAt = this.createdAt,
        meals = this.meals.map { it.toEntity() }
    )
}

fun BranchOrderDTO.toEntity(): BranchOrderEntity {
    return BranchOrderEntity(
        id = this.id,
        title = this.title,
        lat = this.lat,
        lng = this.lng
    )
}

fun PaymentMethodOrderDTO.toEntity(): PaymentMethodOrderEntity {
    return PaymentMethodOrderEntity(
        id = this.id,
        title = this.title
    )
}

fun AddressOrderDTO.toEntity(): AddressOrderEntity {
    return AddressOrderEntity(
        id = this.id,
        region = this.region.toEntity(),
        area = this.area.toEntity(),
        subRegion = this.subRegion,
        street = this.street,
        specialSign = this.specialSign,
        buildingNumber = this.buildingNumber,
        floorNumber = this.floorNumber,
        mainPhone = this.mainPhone,
        phone = this.phone,
        apartmentNumber = this.apartmentNumber,
        extraInfo = this.extraInfo,
        latitude = this.latitude,
        longitude = this.longitude
    )
}

fun RegionOrderDTO.toEntity(): RegionOrderEntity {
    return RegionOrderEntity(
        id = this.id,
        name = this.name
    )
}

fun AreaOrderDTO.toEntity(): AreaOrderEntity {
    return AreaOrderEntity(
        id = this.id,
        name = this.name
    )
}

fun UserDTO.toEntity(): UserOrderEntity {
    return UserOrderEntity(
        id = this.id,
        name = this.name
    )
}

fun MealOrderDTO.toEntity(): MealOrderEntity {
    return MealOrderEntity(
        id = this.id,
        mealTitle = this.mealTitle,
        mealImage = this.mealImage,
        sizeTitle = this.sizeTitle,
        price = this.price,
        quantity = this.quantity,
        subChoicesPrice = this.subChoicesPrice,
        primaryPrice = this.primaryPrice,
        totalPrice = this.totalPrice,
        comment = this.comment,
        choices = this.choices
    )
}