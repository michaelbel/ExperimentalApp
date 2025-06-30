import org.apache.commons.io.output.ByteArrayOutputStream
import java.io.FileInputStream
import java.nio.charset.Charset
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
}

private val gitCommitsCount: Int by lazy {
    when {
        System.getProperty("os.name").contains("Windows", ignoreCase = true) -> 1
        else -> {
            val stdout = ByteArrayOutputStream()
            exec {
                commandLine("git", "rev-list", "--count", "HEAD")
                standardOutput = stdout
            }
            stdout.toString(Charset.defaultCharset()).trim().toInt()
        }
    }
}

kotlin {
    jvmToolchain(libs.versions.jdk.get().toInt())
}

android {
    namespace = "org.michaelbel.theme"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        applicationId = "org.michaelbel.theme"
        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = libs.versions.target.sdk.get().toInt()
        versionCode = gitCommitsCount
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = if (signingConfigs.findByName("release") != null) signingConfigs.getByName("release") else null
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            applicationIdSuffix = ".debug"
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

base {
    archivesName.set("Mobile-Template-v${android.defaultConfig.versionName}(${android.defaultConfig.versionCode})") // Replace with your own app's name
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.google.material)
}

tasks.register("printVersionName") { doLast { println(android.defaultConfig.versionName) } }
tasks.register("printVersionCode") { doLast { println(android.defaultConfig.versionCode.toString()) } }