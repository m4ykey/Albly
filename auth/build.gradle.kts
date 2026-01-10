import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.stability.analyzer)
    alias(libs.plugins.serialization)
}

val localProps = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

val spotifyClientId : String = localProps.getProperty("SPOTIFY_CLIENT_ID")
val spotifyClientSecret : String = localProps.getProperty("SPOTIFY_CLIENT_SECRET")

android {
    namespace = "com.m4ykey.auth"
    compileSdk {
        version = release(36)
    }

    packaging {
        resources.excludes += "META-INF/LICENSE.md"
        resources.excludes += "META-INF/LICENSE-notice.md"
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "SPOTIFY_CLIENT_ID" , "\"${spotifyClientId}\"")
        buildConfigField("String", "SPOTIFY_CLIENT_SECRET" , "\"${spotifyClientSecret}\"")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_18)
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.koin.android)
    implementation(libs.koin.jetpack.compose)

    implementation(libs.bundles.ktor)
    implementation(libs.ktor.auth)

    implementation(libs.kotlinx.datetime)

    implementation(libs.ktx.serialization.json)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.mockk)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}