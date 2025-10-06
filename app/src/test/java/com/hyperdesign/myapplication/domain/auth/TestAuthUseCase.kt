package com.hyperdesign.myapplication.domain.auth

import com.hyperdesign.myapplication.data.dto.LoginResponse
import com.hyperdesign.myapplication.data.dto.User
import com.hyperdesign.myapplication.domain.Entity.LoginEntity
import com.hyperdesign.myapplication.domain.Entity.LoginRequest
import com.hyperdesign.myapplication.domain.Entity.UserEntity
import com.hyperdesign.myapplication.domain.repo.auth.AuthRepo
import com.hyperdesign.myapplication.domain.usecase.auth.LoginUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TestAuthUseCase {



    private val authRepo: AuthRepo = mockk()

    private val loginUseCase: LoginUseCase = LoginUseCase(authRepo)


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
}