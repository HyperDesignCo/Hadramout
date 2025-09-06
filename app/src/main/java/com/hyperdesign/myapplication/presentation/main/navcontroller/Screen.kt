package com.hyperdesign.myapplication.presentation.main.navcontroller

import android.net.Uri
import com.google.gson.Gson
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.data.dto.ForgetPasswordResponseDto
import com.hyperdesign.myapplication.domain.Entity.ForgetPasswordModeEntity
import com.hyperdesign.myapplication.domain.Entity.HomeMenu
import com.hyperdesign.myapplication.domain.Entity.Meal


// Sealed class for all screens
sealed class Screen(val route: String, val title: Int? = null, val icon: Int? = null) {
    object LoginInScreen : Screen(ScreensConst.LOGIN_IN_SCREEN)
    object LoginStepTwoScreen : Screen(ScreensConst.LOGIN_SCREEN_STEP_TWO)
    object ForgotPasswordScreen : Screen(ScreensConst.FORGOT_PASSWORD_SCREEN)
    object VerifyScreen : Screen("${ScreensConst.Verify_Screen}/{verifyJson}/{email}")

    object SignUpScreen : Screen(ScreensConst.Sign_Up_Screen)
    object CartScreen : Screen(ScreensConst.Cart_Screen)

    object MyOrders :Screen(ScreensConst.MY_ORDERS)
    object CheckOutScreen :  Screen(ScreensConst.CHECK_OUT_SCREEN)
    object MealDetailsScreen : Screen("${ScreensConst.MEAL_DETAILS_SCREEN}/{mealJson}")
    object ResetPasswordScreen : Screen("${ScreensConst.RESET_PASSWORD_SCREEN}/{email}")
    object HomeScreen : Screen(ScreensConst.HOME_SCREEN, R.string.home, R.drawable.home)
    object ProfileScreen : Screen(ScreensConst.PROFILE_SCREEN, R.string.profile, R.drawable.profile)
    object MenueScreen : Screen(ScreensConst.MENUE_SCREEN, R.string.menu, R.drawable.menu)

    object SettingsScreen:Screen(ScreensConst.SETTINGS_SCREEN)

    object WhoAreWeScreen:Screen(ScreensConst.WHO_ARE_WE_SCREEN)

    object ContactUsScreen:Screen(ScreensConst.CONTACT_US_SCREEN)

    object ReturnPolicyScreen:Screen(ScreensConst.RETURN_POLICY_SCREEN)

    object TermesAndConditionsScreen:Screen(ScreensConst.TERMS_AND_CONDITIONS_SCREEN)

    object PrivacyPolicyScreen:Screen(ScreensConst.PRIVACY_POLICY_SCREEN)

    object AllAddressesScreen: Screen(ScreensConst.ALL_ADDRESSES_SCREEN)

}




fun goToScreenMealDetails(meal: Meal): String {
    val json = Uri.encode(Gson().toJson(meal))
    return "${ScreensConst.MEAL_DETAILS_SCREEN}/$json"
}

fun goToVerifyScreen(verify: ForgetPasswordModeEntity,email:String):String{
    val json = Uri.encode(Gson().toJson(verify))
    val email = email
    return "${ScreensConst.Verify_Screen}/$json/$email"

}
