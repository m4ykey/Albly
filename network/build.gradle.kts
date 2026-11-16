import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.serialization)
}

val localProps = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

fun buildSecret(name : String) : String =
    localProps.getProperty(name) ?: System.getenv(name) ?: ""

val token : String = localProps.getProperty("token")
val contactMail : String = localProps.getProperty("contact_mail")

android {
    namespace = "com.m4ykey.network"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "token", "\"${token}\"")
        buildConfigField("String", "contact_mail", "\"${contactMail}\"")
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
    implementation(project(":artist"))

    implementation(libs.bundles.ktor)
    implementation(libs.bundles.koin)

    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
}
