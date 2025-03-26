plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.zombies.cowhealthai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.zombies.cowhealthai"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_11 // ✅ Change to Java 11
        targetCompatibility = JavaVersion.VERSION_11 // ✅ Change to Java 11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.common)
    implementation(libs.generativeai)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.core)
    implementation(libs.picasso)
    implementation ("org.json:json:20210307")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
}
