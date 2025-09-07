plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    id ("kotlin-kapt")//Room
    id ("dagger.hilt.android.plugin")   //dagger_hilt
    id ("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
//    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.hyperdesign.myapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.hyperdesign.myapplication"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true

    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.play.services.auth)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.firebase.messaging.ktx)
    //Security
    implementation ("androidx.security:security-crypto:1.1.0-alpha01")

//icons
    implementation("androidx.compose.material:material-icons-extended:1.7.7")
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    implementation("com.google.android.gms:play-services-location:21.0.1")
    //Retrofit

    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)

    implementation (libs.androidx.room.runtime)
    kapt (libs.androidx.room.compiler)
    implementation (libs.androidx.room.ktx)

    //Coil
    implementation(libs.coil.compose)

    //Accompanist
    implementation (libs.accompanist.systemuicontroller)


    //Compose Navigation
    //noinspection GradleDependency
    implementation (libs.androidx.navigation.compose)

    //Dagger Hilt
    implementation ("com.google.dagger:hilt-android:2.51.1")
    kapt ("com.google.dagger:hilt-android-compiler:2.51.1")
    kapt ("androidx.hilt:hilt-compiler:1.1.0")
    implementation ("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation ("androidx.activity:activity-ktx:1.8.1")

    //Splash Api
    implementation (libs.androidx.core.splashscreen)

    //observe as state
    //noinspection GradleDependency
    implementation (libs.androidx.runtime.livedata)

    //Serialization
    implementation(libs.kotlinx.serialization.json)

    //Firebase
    implementation(libs.firebase.bom)
//    implementation(libs.firebase.analytics)
    implementation (libs.firebase.messaging.directboot)
    implementation(libs.firebase.auth)


    //ktor dependencies
    implementation("io.ktor:ktor-client-core:2.3.4")
    implementation("io.ktor:ktor-client-cio:2.3.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
    implementation("io.ktor:ktor-serialization-gson:2.3.4")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("io.ktor:ktor-client-logging:2.3.12")


    //koin
    implementation(libs.bundles.koin)



}