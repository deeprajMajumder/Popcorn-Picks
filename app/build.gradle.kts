import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.popcon.picks"
    compileSdk = 34

    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { stream ->
            localProperties.load(stream)
        }
    }

    val apiKey = localProperties.getProperty("API_KEY")

    defaultConfig {
        applicationId = "com.popcon.picks"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "API_KEY", "\"$apiKey\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
        buildConfig = true
        compose = true
    }
    firebaseCrashlytics {
        mappingFileUploadEnabled = false
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

dependencies {
    implementation (libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation (libs.ui)
    implementation (libs.androidx.material)
    implementation (libs.ui.tooling)
    implementation (libs.androidx.activity.compose)
    implementation (libs.coil.compose)
    implementation (libs.retrofit)
    implementation (libs.logging.interceptor)
    implementation (libs.converter.gson)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.cardview)
    implementation (libs.androidx.navigation.compose)
    //Coil
    implementation(libs.coil.compose)
    //room
    implementation (libs.androidx.room.runtime)
    implementation(libs.firebase.crashlytics)
    annotationProcessor  (libs.androidx.room.compiler)
    implementation (libs.androidx.room.ktx)
    kapt (libs.androidx.room.compiler)
    /**
     * Using hilt for DI, to ensure ease of testability and scalability
     */
    implementation (libs.hilt.android)
    implementation (libs.androidx.hilt.navigation.compose)
    kapt (libs.hilt.compiler)
    kapt (libs.androidx.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}