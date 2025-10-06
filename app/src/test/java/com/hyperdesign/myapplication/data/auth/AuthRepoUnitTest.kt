package com.hyperdesign.myapplication.data.auth

import com.hyperdesign.myapplication.data.dto.LoginResponse
import com.hyperdesign.myapplication.data.dto.User
import com.hyperdesign.myapplication.data.remote.auth.NetworkingApiServices
import com.hyperdesign.myapplication.data.repo.AuthRepo.AuthRepoImpl
import com.hyperdesign.myapplication.domain.Entity.LoginRequest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthRepoUnitTest {


    private val apiService = mockk<NetworkingApiServices>()
    private val authRepo = AuthRepoImpl(apiService)

    @Test
    fun `should return success when login repo is called with valid credentials` ()= runTest {



        val loginRequest = LoginRequest("abdalllahalsayed2gmail.com","123456789")

        val user = User(

            1,
            "abdalllah",
            "abdalllahalsayed2gmail.com",
            image = "",
            mobile = ""

        )

        coEvery { apiService.login(loginRequest) } returns LoginResponse(
            "","",user,"success"
        )



       val result= authRepo.login(loginRequest)

        assertEquals("success",result.message)



    }

    @Test
    fun `ShouldRetuenInvalidLoginWhenCalledLoginRepoWithInvalidCredentials`()=runTest {

        //Given
        val loginRequest = LoginRequest("abdalllah","")

        val user = User(

            2,
            "",
            "",
            image = "",
            mobile = ""

        )
        coEvery { apiService.login(loginRequest) } returns LoginResponse(
            "","",user,"faild"

        )

        //when
        val result = authRepo.login(loginRequest)

        assertEquals("faild",result.message)


    }
}