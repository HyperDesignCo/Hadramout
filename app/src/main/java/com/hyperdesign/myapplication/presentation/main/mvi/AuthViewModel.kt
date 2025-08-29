package com.hyperdesign.myapplication.presentation.main.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.data.local.TokenManager
import com.hyperdesign.myapplication.domain.Entity.ForgetPasswordRequest
import com.hyperdesign.myapplication.domain.usecase.auth.RefreshTokenUseCase
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginStateModel
import com.hyperdesign.myapplication.presentation.menu.mvi.MenuStateModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthViewModel(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
): ViewModel() {

    private var _authState = MutableStateFlow(LoginStateModel())
    val authState : StateFlow<LoginStateModel> = _authState

    suspend fun handelIntent(intent: RefreshTOkenIntents){
        when(intent) {
            is RefreshTOkenIntents.RefreshToken -> {
                checkUserState()
            }
        }

    }

    suspend fun checkUserState(): String {

        val userData = tokenManager.getUserData()
        Log.i("status", "checkUserState: "+""+userData)
        return when {
            userData == null -> {
                // No user found
                "login"
            }



            else -> {
                // User exists

                val refreshResult = try {
//                    loading.value=true

                    val refreshTokenRequest = ForgetPasswordRequest(
                        email = userData.email
                    )
                    val result = refreshTokenUseCase.invoke(refreshTokenRequest)
                    _authState.value = _authState.value.copy(
                        loginResponse = result
                    )
                    tokenManager.deleteUserData()
                    tokenManager.saveUserData(result.user)

//                    if (result.user?.status != "active") {
//                        // Navigate to StatusFragment if not active
//                        return "status"
//                    }


                    // navigate based on verification status
//                    if (result.doctor.phoneVerify == 1||result.doctor.status=="active") "home"
//                    else "verify"
                    "home"
                } catch (e: HttpException) {
                    Log.d("error","${e.message}")

                    "error"
                } catch (e: Exception) {
                    Log.d("error","${e.message}")
                    "error"
                }
                refreshResult
            }

        }
    }

}