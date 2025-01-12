plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.aman.swipeassignment"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.aman.swipeassignment"
        minSdk = 26
        targetSdk = 35
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
    buildFeatures{
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Jetpack Navigation Component
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    //Circular ImageView
    implementation (libs.circleimageview)

    // Coroutines
    implementation (libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // COIL
    implementation(libs.coil)

    // KOIN
    implementation (libs.koin.core)
    implementation (libs.koin.android)

    // Retrofit
    implementation (libs.retrofit)
    // GSON
    implementation (libs.converter.gson)
    // Okhttp
    implementation(platform(libs.okhttp.bom))
    // Moshi
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)

}