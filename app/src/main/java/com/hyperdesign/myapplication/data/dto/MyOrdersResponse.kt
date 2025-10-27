package com.hyperdesign.myapplication.data.dto

import com.google.gson.annotations.SerializedName

data class OrdersResponseDTO(
    @SerializedName("orders")
    val orders: List<OrderDTO>? = emptyList(), // Made nullable with default empty list to handle null orders

    @SerializedName("message")
    val message: String? = null // Made nullable to handle absence in JSON
)

data class OrderDTO(
    @SerializedName("id")
    val id: String, // Non-null, as JSON always provides it

    @SerializedName("branch")
    val branch: BranchOrderDTO?, // Made nullable for safety

    @SerializedName("payment_method")
    val paymentMethod: PaymentMethodOrderDTO? = null,

    @SerializedName("address")
    val address: AddressOrderDTO? = null,

    @SerializedName("distance")
    val distance: String? = null, // Made nullable, as "unkwonen" might be null in some cases

    @SerializedName("status")
    val status: String? = null, // Made nullable for safety

    @SerializedName("status_code")
    val statusCode: Int? = null, // Made nullable for safety

    @SerializedName("payment_status")
    val paymentStatus: String? = null, // Made nullable, as it’s empty in JSON

    @SerializedName("user")
    val user: UserDTO?, // Made nullable for safety

    @SerializedName("referenceNumber")
    val referenceNumber: String? = null, // Made nullable, as it’s empty in JSON

    @SerializedName("primary_price")
    val primaryPrice: String? = null, // Made nullable for safety

    @SerializedName("service_charge_cost")
    val serviceChargeCost: String? = null, // Made nullable for safety

    @SerializedName("vat_cost")
    val vatCost: String? = null, // Made nullable for safety

    @SerializedName("delivery_cost")
    val deliveryCost: String? = null, // Made nullable for safety

    @SerializedName("coupon")
    val coupon: String? = null, // Made nullable, as it’s empty in JSON

    @SerializedName("coupon_cost")
    val couponCost: String? = null, // Made nullable for safety

    @SerializedName("offer_discount")
    val offerDiscount: String? = null, // Made nullable for safety

    @SerializedName("net_price")
    val netPrice: String? = null, // Made nullable for safety

    @SerializedName("created_at")
    val createdAt: String? = null, // Made nullable for safety

    @SerializedName("meals")
    val meals: List<MealOrderDTO>? = emptyList() // Made nullable with default empty list
)

data class BranchOrderDTO(
    @SerializedName("id")
    val id: Int? = null, // Made nullable for safety

    @SerializedName("title")
    val title: String? = null, // Made nullable for safety

    @SerializedName("lat")
    val lat: String? = null, // Made nullable for safety

    @SerializedName("lng")
    val lng: String? = null // Made nullable for safety
)

data class PaymentMethodOrderDTO(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("title")
    val title: String? = null
)

data class AddressOrderDTO(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("region")
    val region: RegionOrderDTO? = null,

    @SerializedName("area")
    val area: AreaOrderDTO? = null,

    @SerializedName("sub_region")
    val subRegion: String? = null,

    @SerializedName("street")
    val street: String? = null,

    @SerializedName("special_sign")
    val specialSign: String? = null,

    @SerializedName("building_number")
    val buildingNumber: String? = null,

    @SerializedName("floor_number")
    val floorNumber: String? = null,

    @SerializedName("main_phone")
    val mainPhone: String? = null,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("apartment_number")
    val apartmentNumber: String? = null,

    @SerializedName("extra_info")
    val extraInfo: String? = null,

    @SerializedName("latitude")
    val latitude: String? = null,

    @SerializedName("longitude")
    val longitude: String? = null
)

data class RegionOrderDTO(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("name")
    val name: String? = null
)

data class AreaOrderDTO(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("name")
    val name: String? = null
)

data class UserDTO(
    @SerializedName("id")
    val id: String? = null, // Made nullable for safety

    @SerializedName("name")
    val name: String? = null // Made nullable for safety
)

data class MealOrderDTO(
    @SerializedName("id")
    val id: String? = null, // Made nullable for safety

    @SerializedName("meal_title")
    val mealTitle: String? = null, // Made nullable for safety

    @SerializedName("meal_image")
    val mealImage: String? = null, // Made nullable for safety

    @SerializedName("size_title")
    val sizeTitle: String? = null, // Made nullable, as it’s empty in some cases

    @SerializedName("price")
    val price: String? = null, // Made nullable for safety

    @SerializedName("quantity")
    val quantity: String? = null, // Made nullable for safety

    @SerializedName("sub_choices_price")
    val subChoicesPrice: String? = null, // Made nullable for safety

    @SerializedName("primary_price")
    val primaryPrice: String? = null, // Made nullable for safety

    @SerializedName("total_price")
    val totalPrice: String? = null, // Made nullable for safety

    @SerializedName("comment")
    val comment: String? = null, // Made nullable, as it’s empty in JSON

    @SerializedName("choices")
    val choices: List<String>? = emptyList() // Made nullable with default empty list
)