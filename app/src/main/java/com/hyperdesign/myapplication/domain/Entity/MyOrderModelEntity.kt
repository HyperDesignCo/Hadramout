package com.hyperdesign.myapplication.domain.Entity

data class OrdersResponseEntity(
    val orders: List<OrderEntity>? = emptyList(),
    val message: String? = null
)

data class OrderEntity(
    val id: String? = null,
    val branch: BranchOrderEntity? = null,
    val paymentMethod: PaymentMethodOrderEntity? = null,
    val address: AddressOrderEntity? = null,
    val distance: String? = null,
    val status: String? = null,
    val statusCode: Int? = null,
    val paymentStatus: String? = null,
    val user: UserOrderEntity? = null,
    val referenceNumber: String? = null,
    val primaryPrice: String? = null,
    val serviceChargeCost: String? = null,
    val vatCost: String? = null,
    val deliveryCost: String? = null,
    val coupon: String? = null,
    val couponCost: String? = null,
    val offerDiscount: String? = null,
    val netPrice: String? = null,
    val createdAt: String? = null,
    val meals: List<MealOrderEntity>? = emptyList()
)

data class BranchOrderEntity(
    val id: Int? = null,
    val title: String? = null,
    val lat: String? = null,
    val lng: String? = null
)

data class PaymentMethodOrderEntity(
    val id: Int? = null,
    val title: String? = null
)

data class AddressOrderEntity(
    val id: String? = null,
    val region: RegionOrderEntity? = null,
    val area: AreaOrderEntity? = null,
    val subRegion: String? = null,
    val street: String? = null,
    val specialSign: String? = null,
    val buildingNumber: String? = null,
    val floorNumber: String? = null,
    val mainPhone: String? = null,
    val phone: String? = null,
    val apartmentNumber: String? = null,
    val extraInfo: String? = null,
    val latitude: String? = null,
    val longitude: String? = null
)

data class RegionOrderEntity(
    val id: Int? = null,
    val name: String? = null
)

data class AreaOrderEntity(
    val id: Int? = null,
    val name: String? = null
)

data class UserOrderEntity(
    val id: String? = null,
    val name: String? = null
)

data class MealOrderEntity(
    val id: String? = null,
    val mealTitle: String? = null,
    val mealImage: String? = null,
    val sizeTitle: String? = null,
    val price: String? = null,
    val quantity: String? = null,
    val subChoicesPrice: String? = null,
    val primaryPrice: String? = null,
    val totalPrice: String? = null,
    val comment: String? = null,
    val choices: List<String>? = emptyList()
)