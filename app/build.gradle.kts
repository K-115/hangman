plugins {
    alias(libs.plugins.android.application)
    // Implicitly applies the Kotlin plugin required for compilation
//    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.hangman"
    compileSdk = 35 // Aligned to latest modern Android SDK stability standards

    defaultConfig {
        applicationId = "com.example.hangman"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            excludes.add("META-INF/DEPENDENCIES")
            excludes.add("META-INF/LICENSE*")
            excludes.add("META-INF/NOTICE*")
            excludes.add("javax/annotation/**")
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // REMOVED: implementation(libs.appcrawler.platform) has been deleted from here

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Correctly structured sandboxed testing framework wrapper
    androidTestImplementation("com.google.android.appcrawler:appcrawler-platform:0.0.1-alpha03") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
    }
}

configurations.all {
    exclude(group = "com.google.code.findbugs", module = "jsr305")
    exclude(group = "com.google.auto.value", module = "auto-value")

    // Enforce matching Kotlin runtime versions to prevent background variant collisions
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion("2.2.10") // Aligned with contemporary Kotlin Compose compiler plugins
        }
    }
}
