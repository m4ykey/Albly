import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
}

val localProps = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

val token : String = localProps.getProperty("token")

android {
    namespace = "com.m4ykey.network"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "token", "\"${token}\"")
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":search"))
    implementation(project(":lyrics"))
    implementation(project(":album"))

    implementation(libs.bundles.ktor)
    implementation(libs.bundles.koin)
}