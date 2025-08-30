package com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi

sealed class CreatePasswordIntents {

    data class onChangeNewPassword(val newPassword:String):CreatePasswordIntents()

    data class changePasswordClick(val Password:String):CreatePasswordIntents()


}