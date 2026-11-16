import com.diffplug.gradle.spotless.SpotlessExtension
import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.about.libraries) apply false
    alias(libs.plugins.stability.analyzer) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.spotless)
}

configure<SpotlessExtension> {
    kotlinGradle {
        target("*.gradle.kts")
        ktlint(libs.versions.ktlint.get())
    }
}

val detektPluginId = libs.plugins.detekt.get().pluginId
val spotlessPluginId = libs.plugins.spotless.get().pluginId

subprojects {
    apply(plugin = detektPluginId)
    apply(plugin = spotlessPluginId)

    configure<SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            targetExclude("build/**")
            ktlint(libs.versions.ktlint.get())
        }
    }

    configure<DetektExtension> {
        buildUponDefaultConfig = true
        config.setFrom(rootProject.file("config/detekt/detekt.yml"))
        source.setFrom(
            files(
                "src/main/java",
                "src/main/kotlin",
                "src/test/java",
                "src/test/kotlin",
                "src/androidTest/java",
                "src/androidTest/kotlin"
            )
        )
    }

    tasks.withType<Detekt>().configureEach {
        jvmTarget.set("21")
        reports {
            html.required.set(true)
            checkstyle.required.set(true)
            sarif.required.set(true)
            markdown.required.set(true)
        }
    }
}
