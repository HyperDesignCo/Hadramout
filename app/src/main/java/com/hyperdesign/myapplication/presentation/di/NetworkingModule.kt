package com.example.doctorona.data.di

import com.hyperdesign.myapplication.data.local.TokenManager
import com.hyperdesign.myapplication.data.remote.auth.NetworkingApiServices
import com.hyperdesign.myapplication.data.remote.auth.NetworkingApiServicesImpl
import com.hyperdesign.myapplication.presentation.utilies.Constants.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging

import io.ktor.serialization.gson.gson
import org.koin.dsl.module
import java.util.Locale

val networkingModule = module {

    single {

        HttpClient(CIO) {
            defaultRequest {
                url(BASE_URL)
                val lang = get<TokenManager>().getLanguage() ?: Locale.getDefault().language
                headers.append("lang", lang)

                val token = get<TokenManager>().getAccessToken()
                if (!token.isNullOrEmpty()) {
                    headers.append("Authorization", "Bearer $token")
                }
            }
            install(ContentNegotiation){
                gson()
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

        }

    }

    single<NetworkingApiServices> {

        NetworkingApiServicesImpl(get())

    }



}
