package com.hyperdesign.myapplication.domain.auth

import com.hyperdesign.myapplication.data.dto.LoginResponse
import com.hyperdesign.myapplication.data.dto.RegisterResponse
import com.hyperdesign.myapplication.data.dto.User
import com.hyperdesign.myapplication.domain.Entity.LoginEntity
import com.hyperdesign.myapplication.domain.Entity.LoginRequest
import com.hyperdesign.myapplication.domain.Entity.RegisterModelEntity
import com.hyperdesign.myapplication.domain.Entity.RegisterRequst
import com.hyperdesign.myapplication.domain.Entity.UserEntity
import com.hyperdesign.myapplication.domain.repo.auth.AuthRepo
import com.hyperdesign.myapplication.domain.usecase.auth.LoginUseCase
import com.hyperdesign.myapplication.domain.usecase.auth.RegisterUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TestAuthUseCase {



    private val authRepo: AuthRepo = mockk()

    private val loginUseCase: LoginUseCase = LoginUseCase(authRepo)

    private val registerUseCase: RegisterUseCase = RegisterUseCase(authRepo)


    @Test
    fun `ShouldReturnSuccessWhenCaledLoginUseCaseWithValidCredntials`()= runTest{
        //Given
        val loginRequest = LoginRequest("abdalllahalsayed2gmail.com","123456789")
        val user = UserEntity(

            1,
            "abdalllah",
            "abdalllahalsayed2gmail.com",
            image = "",
            mobile = ""

        )
        coEvery { authRepo.login(loginRequest) }returns LoginEntity(
            "","",user,"success"

        )


        //when
        val result = loginUseCase(loginRequest)

        //then
        assertEquals("success",result.message)

    }

    @Test
    fun`ShouldReturnSuccessRegisterWhenCalledRegisterUseCaseInvoke`()=runTest{
        //given
        val registerRequest = RegisterRequst(email = "abdallahalsayed2@gamaild.com","123456789", name = "abdo", mobile = "01151828780","")
        val user = UserEntity(

            1,
            "abdalllah",
            "abdalllahalsayed2gmail.com",
            image = "",
            mobile = ""

        )
        coEvery { authRepo.register(registerRequest) } returns RegisterModelEntity(
            accessToken = "",
            tokenType = "",
            user = user,
            message = "SuccessRegister"
        )


        //action
        val result = registerUseCase(registerRequest)
        
        //then
        assertEquals("SuccessRegister",result.message)

    }
}