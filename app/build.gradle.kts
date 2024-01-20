import org.jetbrains.kotlin.de.undercouch.gradle.tasks.download.Download
import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.gpt222"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gpt222"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks {
    val downloadVocab by creating(Download::class) {
        onlyIf { !file("$projectDir/src/main/assets/gpt2-vocab.json").exists() }
        src("https://s3.amazonaws.com/models.huggingface.co/bert/gpt2-vocab.json")
        dest("$projectDir/src/main/assets/gpt2-vocab.json")
    }
    val downloadModel by creating(Download::class) {
        onlyIf { !file("$projectDir/src/main/assets/model.tflite").exists() }
        src("https://s3.amazonaws.com/models.huggingface.co/bert/gpt2-64-fp16.tflite")
        dest("$projectDir/src/main/assets/model.tflite")
    }
    val downloadMerges by creating(Download::class) {
        onlyIf { !file("$projectDir/src/main/assets/gpt2-merges.txt").exists() }
        src("https://s3.amazonaws.com/models.huggingface.co/bert/gpt2-merges.txt")
        dest("$projectDir/src/main/assets/gpt2-merges.txt")
    }
    whenTaskAdded {
        if (name in listOf("assembleDebug", "assembleRelease")) {
            dependsOn(downloadVocab)
            dependsOn(downloadModel)
            dependsOn(downloadMerges)
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // gpt2
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Seems like older versions of TF work faster!
    // 2.7.0 fast, 2.8.0 fast!
    implementation("org.tensorflow:tensorflow-lite:2.8.0")

    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    //implementation("androidx.activity:activity-ktx:1.8.2")
}