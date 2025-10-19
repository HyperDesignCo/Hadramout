import com.hyperdesign.myapplication.presentation.utilies.getSecret

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.dagger.hilt.android")
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

    secrets {
        propertiesFileName = "secrets.properties"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "MAPS_API_KEY", getSecret("GOOGLE_MAPS_API_KEY", "\"EMPTY\""))
            buildConfigField("String", "API_KEY", getSecret("API_KEY", "\"EMPTY\""))
            buildConfigField("boolean", "IS_PRODUCTION", "true")
            manifestPlaceholders["MAPS_API_KEY"] = getSecret("GOOGLE_MAPS_API_KEY", "EMPTY")
        }
        debug {
            isDebuggable = true
            buildConfigField("String", "MAPS_API_KEY", getSecret("GOOGLE_MAPS_API_KEY", "\"EMPTY\""))
            buildConfigField("String", "API_KEY", getSecret("API_KEY", "\"EMPTY\""))
            buildConfigField("boolean", "IS_PRODUCTION", "false")
            manifestPlaceholders["MAPS_API_KEY"] = getSecret("GOOGLE_MAPS_API_KEY", "EMPTY")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

//    kotlin {
//        compilerOptions {
//            jvmTarget.set(JavaVersion.VERSION_17)  // Updated to JVM 17
//        }
//    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
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
    ksp (libs.androidx.room.compiler)
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
    ksp ("com.google.dagger:hilt-android-compiler:2.51.1")
    ksp ("androidx.hilt:hilt-compiler:1.1.0")
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

    // MockK for unit testing
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.turbine)
    testImplementation(kotlin("test"))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.compiler)
    ksp("androidx.hilt:hilt-compiler:1.2.0")

    //map
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(libs.maps.compose)
    implementation(libs.places)
    implementation(libs.accompanist.permissions)
}

