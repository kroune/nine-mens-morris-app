plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("io.gitlab.arturbosch.detekt") version "1.23.3"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

composeCompiler {
    enableStrongSkippingMode = true
}

android {
    namespace = "com.kroune.nineMensMorrisApp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kroune.nineMensMorrisApp"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.1"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildToolsVersion = "34.0.0"
    compileSdk = 34
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.kotlinx.serialization.json)

    // ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.ohhttp)
    implementation(libs.ktor.client.logging)

    // test dependencies
    androidTestImplementation(libs.androidx.ui.test.junit4)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    debugImplementation(libs.androidx.ui.test.manifest)

    // my own dependencies
    implementation(libs.x.men.s.morris.lib)
    implementation(libs.x.men.s.morris.shared)

    // di
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
}

kapt {
    correctErrorTypes = true
}
