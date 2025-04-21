import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
import kotlin.io.encoding.ExperimentalEncodingApi

val appVersion: String = "1.0.1"

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.baseline.profile)
//    alias(libs.plugins.compose.compiler.report.generator)
//    id("org.jetbrains.compose.hot-reload") version "1.0.0-alpha03"
//    alias(libs.plugins.storytale)
}

composeCompiler {
    featureFlags = setOf(
        ComposeFeatureFlag.OptimizeNonSkippingGroups,
        ComposeFeatureFlag.PausableComposition
    )
}

dependencies {
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.ui)
    implementation(libs.decompose)
    implementation(libs.decompose.jetbrains)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.protobuf)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.multiplatform.settings)
    implementation(libs.multiplatform.settings.no.arg)
    implementation(libs.ninemensmorris)
    implementation(libs.filekit.compose)
//            implementation(compose.components.uiToolingPreview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.ktor.client.cio)
//            implementation(compose.uiTooling)
//            implementation(compose.preview)
    androidTestImplementation(kotlin("test"))
    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
    androidTestImplementation(compose.uiTest)
}

@OptIn(ExperimentalEncodingApi::class)
android {
    namespace = "io.github.kroune.nine_mens_morris_kmp_app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "io.github.kroune.nine_mens_morris_kmp_app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = appVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "DebugProbesKt.bin"
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        create("release") {
            keyAlias = "release"
            if (System.getenv("KEYSTORE") != null && System.getenv("KEYSTORE_PASSWORD") != null) {
                storeFile = File(project.projectDir.absolutePath, "keyStore.jks")
                storePassword = System.getenv("KEYSTORE_PASSWORD")!!
                keyPassword = System.getenv("KEYSTORE_PASSWORD")!!
            } else {
                storeFile = file("/home/olowo/secureKeystore.jks")
                storePassword = file("/home/olowo/secureSignPass").readText().trim()
                keyPassword = file("/home/olowo/secureSignPass").readText().trim()
            }
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
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
    //https://developer.android.com/develop/ui/compose/testing#setup
    dependencies {
        debugImplementation(libs.androidx.ui.test.manifest)
        androidTestImplementation(libs.androidx.ui.test)
        "baselineProfile"(project(":baselineprofile"))
    }
    baselineProfile {
        baselineProfileOutputDir = "../androidMain/generated/baselineProfiles"
        automaticGenerationDuringBuild = true
    }
    @Suppress("UnstableApiUsage")
    testOptions {
        managedDevices {
            localDevices {
                create("pixel2api30") {
                    // Use device profiles you typically see in Android Studio.
                    device = "Pixel 2"
                    // Use only API levels 27 and higher.
                    apiLevel = 30
                    // To include Google services, use "google".
                    systemImageSource = "aosp"
                }
            }
        }
    }
}
