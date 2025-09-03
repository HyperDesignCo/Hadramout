package com.hyperdesign.myapplication.data.dto

import com.google.gson.annotations.SerializedName

data class OrdersResponseDTO(
    @SerializedName("orders")
    val orders: List<OrderDTO>,

    @SerializedName("message")
    val message: String
)

data class OrderDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("branch")
    val branch: BranchOrderDTO,

    @SerializedName("payment_method")
    val paymentMethod: PaymentMethodOrderDTO?,

    @SerializedName("address")
    val address: AddressOrderDTO?,

    @SerializedName("distance")
    val distance: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("payment_status")
    val paymentStatus: String,

    @SerializedName("user")
    val user: UserDTO,

    @SerializedName("referenceNumber")
    val referenceNumber: String,

    @SerializedName("primary_price")
    val primaryPrice: String,

    @SerializedName("service_charge_cost")
    val serviceChargeCost: String,

    @SerializedName("vat_cost")
    val vatCost: String,

    @SerializedName("delivery_cost")
    val deliveryCost: String,

    @SerializedName("coupon")
    val coupon: String,

    @SerializedName("coupon_cost")
    val couponCost: String,

    @SerializedName("offer_discount")
    val offerDiscount: String,

    @SerializedName("net_price")
    val netPrice: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("meals")
    val meals: List<MealOrderDTO>
)

data class BranchOrderDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("lat")
    val lat: String,

    @SerializedName("lng")
    val lng: String
)

data class PaymentMethodOrderDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String
)

data class AddressOrderDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("region")
    val region: RegionOrderDTO,

    @SerializedName("area")
    val area: AreaOrderDTO,

    @SerializedName("sub_region")
    val subRegion: String,

    @SerializedName("street")
    val street: String,

    @SerializedName("special_sign")
    val specialSign: String,

    @SerializedName("building_number")
    val buildingNumber: String,

    @SerializedName("floor_number")
    val floorNumber: String,

    @SerializedName("main_phone")
    val mainPhone: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("apartment_number")
    val apartmentNumber: String,

    @SerializedName("extra_info")
    val extraInfo: String,

    @SerializedName("latitude")
    val latitude: String,

    @SerializedName("longitude")
    val longitude: String
)

data class RegionOrderDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
)

data class AreaOrderDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
)

data class UserDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String
)

data class MealOrderDTO(
    @SerializedName("id")
    val id: String,

    @SerializedName("meal_title")
    val mealTitle: String,

    @SerializedName("meal_image")
    val mealImage: String,

    @SerializedName("size_title")
    val sizeTitle: String,

    @SerializedName("price")
    val price: String,

    @SerializedName("quantity")
    val quantity: String,

    @SerializedName("sub_choices_price")
    val subChoicesPrice: String,

    @SerializedName("primary_price")
    val primaryPrice: String,

    @SerializedName("total_price")
    val totalPrice: String,

    @SerializedName("comment")
    val comment: String,

    @SerializedName("choices")
    val choices: List<String>
)



