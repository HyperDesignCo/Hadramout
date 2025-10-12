package com.hyperdesign.myapplication.presentation.auth.viewmodel

import android.content.Context
import app.cash.turbine.test
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.data.local.TokenManager
import com.hyperdesign.myapplication.domain.Entity.LoginEntity
import com.hyperdesign.myapplication.domain.Entity.LoginRequest
import com.hyperdesign.myapplication.domain.Entity.RegisterModelEntity
import com.hyperdesign.myapplication.domain.Entity.RegisterRequst
import com.hyperdesign.myapplication.domain.Entity.UserEntity
import com.hyperdesign.myapplication.domain.usecase.auth.LoginUseCase
import com.hyperdesign.myapplication.domain.usecase.auth.RegisterUseCase
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginIntents
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginStateModel
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginViewModel
import com.hyperdesign.myapplication.presentation.auth.signup.mvi.RegisterIntents
import com.hyperdesign.myapplication.presentation.auth.signup.mvi.RegisterViewModel
import com.hyperdesign.myapplication.presentation.utilies.ValidatePhoneNumber
import com.hyperdesign.myapplication.presentation.utilies.ValidateText
import com.hyperdesign.myapplication.presentation.utilies.ValidationEvent
import com.hyperdesign.myapplication.presentation.utilies.ValidationResult
import dagger.hilt.android.qualifiers.ApplicationContext
import io.mockk.every
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val loginUseCase: LoginUseCase = mockk()
    private val registerUseCase: RegisterUseCase = mockk()

    private val validateText: ValidateText = mockk()
    private val tokenManager: TokenManager = mockk()

    private val validatePhoneNumber: ValidatePhoneNumber =mockk()
    private val context: Context = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var registerViewModel: RegisterViewModel

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

        registerViewModel = RegisterViewModel(
            validateText = validateText,
            validatePhoneNumber = validatePhoneNumber,
            tokenManager = tokenManager,
            registerUseCase = registerUseCase,
            context = context,
        )

        // Mock context string resources
        every { context.getString(R.string.please_enter_your_email_or_phone_number) } returns "Please enter your email or phone number"
        every { context.getString(R.string.please_enter_your_password) } returns "Please enter your password"
        every { context.getString(R.string.enter_email_address) } returns "Enter email address"
        every { context.getString(R.string.enter_your_password) } returns "Enter your password"
        every { context.getString(R.string.enter_your_name) } returns "Enter your name"
        every { context.getString(R.string.invalid_phone_number_format) } returns "Invalid phone number format"
        every { context.getString(R.string.enter_your_confirm_password) } returns "Enter your confirm password"
        every { context.getString(R.string.confirm_password_is_should_be_same_as_password) } returns "Confirm password should be same as password"
        every { context.getString(R.string.email_is_already_used) } returns "Email is already used"

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

    @Test
    fun `ShouldReturnSuccessRegisterWhenCallRegistwerWithValidCredentiales`() = runTest {
        // Given
        val registerRequest = RegisterRequst(
            email = "abdallahalsayed2@gmail.com",
            password = "123456789",
            name = "abdo",
            mobile = "01151828780",
            device_token = ""
        )
        val user = UserEntity(
            id = 1,
            name = "abdalllah",
            email = "abdallahalsayed2@gmail.com",
            image = "",
            mobile = ""
        )
        coEvery { validatePhoneNumber.execute(any()) } returns ValidationResult(successful = true)
        coEvery { validateText.execute(any()) } returns ValidationResult(successful = true)
        coEvery { registerUseCase.invoke(any()) } answers {
            val request = firstArg<RegisterRequst>()
            assertEquals(registerRequest.email, request.email)
            assertEquals(registerRequest.password, request.password)
            assertEquals(registerRequest.name, request.name)
            assertEquals(registerRequest.mobile, request.mobile)
            assertEquals(registerRequest.device_token, request.device_token)
            RegisterModelEntity(
                accessToken = "",
                tokenType = "",
                user = user,
                message = "register success"
            )
        }

        // Set initial state with confirmPassword to match password
        registerViewModel.onIntentEvent(
            RegisterIntents.EmailChanged(email = registerRequest.email)
        )
        registerViewModel.onIntentEvent(
            RegisterIntents.PasswordChanged(password = registerRequest.password)
        )
        registerViewModel.onIntentEvent(
            RegisterIntents.NameChanged(name = registerRequest.name)
        )
        registerViewModel.onIntentEvent(
            RegisterIntents.MobileChanged(mobile = registerRequest.mobile)
        )
        registerViewModel.onIntentEvent(
            RegisterIntents.ConfirmPasswordChanged(confirmPassword = registerRequest.password)
        )

        // Action
        registerViewModel.onIntentEvent(
            RegisterIntents.RegisterClicked(
                email = registerRequest.email,
                password = registerRequest.password,
                name = registerRequest.name,
                mobile = registerRequest.mobile
            )
        )
        advanceUntilIdle()

        // Then
        registerViewModel.state.test {
            val state = awaitItem()
            println("State: $state") // Log the state
            assertEquals(
                /* expected = */ RegisterModelEntity(
                    accessToken = "",
                    tokenType = "",
                    user = user,
                    message = "register success"
                ),
                /* actual = */ state.registerRespnse
            )
            assertFalse(state.isLoading)
            cancelAndIgnoreRemainingEvents()
        }

        // Verify validation event
        registerViewModel.validationEvent.test {
            val event = awaitItem()
            println("Validation Event: $event") // Log validation event
            assertTrue(event is ValidationEvent.Success)
            cancelAndIgnoreRemainingEvents()
        }

        // Verify that registerUseCase was called
        coVerify { registerUseCase.invoke(any()) }
    }

}