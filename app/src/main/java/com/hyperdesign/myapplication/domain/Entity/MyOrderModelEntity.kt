package com.hyperdesign.myapplication.domain.Entity

data class OrdersResponseEntity(
    val orders: List<OrderEntity>,
    val message: String
)

data class OrderEntity(
    val id: String,
    val branch: BranchOrderEntity,
    val paymentMethod: PaymentMethodOrderEntity?,
    val address: AddressOrderEntity?,
    val distance: String,
    val status: String,
    val statusCode: Int,
    val paymentStatus: String,
    val user: UserOrderEntity,
    val referenceNumber: String,
    val primaryPrice: String,
    val serviceChargeCost: String,
    val vatCost: String,
    val deliveryCost: String,
    val coupon: String,
    val couponCost: String,
    val offerDiscount: String,
    val netPrice: String,
    val createdAt: String,
    val meals: List<MealOrderEntity>
)

data class BranchOrderEntity(
    val id: Int,
    val title: String,
    val lat: String,
    val lng: String
)

data class PaymentMethodOrderEntity(
    val id: Int,
    val title: String
)

data class AddressOrderEntity(
    val id: String,
    val region: RegionOrderEntity,
    val area: AreaOrderEntity,
    val subRegion: String,
    val street: String,
    val specialSign: String,
    val buildingNumber: String,
    val floorNumber: String,
    val mainPhone: String,
    val phone: String,
    val apartmentNumber: String,
    val extraInfo: String,
    val latitude: String,
    val longitude: String
)

data class RegionOrderEntity(
    val id: Int,
    val name: String
)

data class AreaOrderEntity(
    val id: Int,
    val name: String
)

data class UserOrderEntity(
    val id: String,
    val name: String
)

data class MealOrderEntity(
    val id: String,
    val mealTitle: String,
    val mealImage: String,
    val sizeTitle: String,
    val price: String,
    val quantity: String,
    val subChoicesPrice: String,
    val primaryPrice: String,
    val totalPrice: String,
    val comment: String,
    val choices: List<String>
)