package com.hyperdesign.myapplication.presentation.auth.viewmodel

import android.content.Context
import app.cash.turbine.test
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.data.local.TokenManager
import com.hyperdesign.myapplication.domain.Entity.LoginEntity
import com.hyperdesign.myapplication.domain.Entity.LoginRequest
import com.hyperdesign.myapplication.domain.Entity.UserEntity
import com.hyperdesign.myapplication.domain.usecase.auth.LoginUseCase
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginIntents
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginStateModel
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginViewModel
import com.hyperdesign.myapplication.presentation.utilies.ValidateText
import com.hyperdesign.myapplication.presentation.utilies.ValidationEvent
import com.hyperdesign.myapplication.presentation.utilies.ValidationResult
import dagger.hilt.android.qualifiers.ApplicationContext
import io.mockk.every
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val loginUseCase: LoginUseCase = mockk()
    private val validateText: ValidateText = mockk()
    private val tokenManager: TokenManager = mockk()
    private val context: Context = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        // Initialize ViewModel with test dispatcher
        loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            validateText = validateText,
            tokenManager = tokenManager,
            context = context,
            dispatcher = testDispatcher
        )

        // Mock context string resources
        every { context.getString(R.string.please_enter_your_email_or_phone_number) } returns "Please enter your email or phone number"
        every { context.getString(R.string.please_enter_your_password) } returns "Please enter your password"

        // Mock tokenManager behavior
        coEvery { tokenManager.saveAccessToken(any()) } returns Unit
        coEvery { tokenManager.saveUserData(any()) } returns Unit
    }

    @Test
    fun `should return success when called with valid credentials`() = runTest {
        // Given
        val email = "abdalllahalsayed2gmail.com"
        val password = "123456789"
        val loginRequest = LoginRequest(email = email, password = password, deviceToken = "")
        val user = UserEntity(1, "abdalllah", email, "", "")
        val loginResponse = LoginEntity("token123", "", user, "login success")

        every { validateText.execute(any()) } returns ValidationResult(successful = true)
        coEvery { loginUseCase.invoke(loginRequest) } returns loginResponse

        // When
        loginViewModel.onIntent(LoginIntents.LoginEvent(phone = email, pass = password))

        // Let coroutines complete
        advanceUntilIdle()

        // Then

        loginViewModel.state.test {
            val finalState = awaitItem() // You may need to skipItems(2) depending on emissions
            assertEquals(false, finalState.isLoading)
            assertEquals(loginResponse, finalState.loginResponse)
            cancelAndConsumeRemainingEvents()
        }

        loginViewModel.validationEvents.test {
            val event = awaitItem()
            assertTrue(event is ValidationEvent.Success)
            cancelAndConsumeRemainingEvents()
        }
    }

}