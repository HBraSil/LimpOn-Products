plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.produtosdelimpeza"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.produtosdelimpeza"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
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
    kotlinOptions {
        compileOptions {
            jvmTarget = "21"
        }
    }
    buildFeatures {
        compose = true
    }

    room {
        // Define o diretório onde o Room salvará os arquivos JSON do esquema.
        // O local recomendado é dentro da pasta 'schemas' do seu projeto.
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    // ICON LIBRARY
    implementation(libs.androidx.material.icons)

    // NAVIGATION
    implementation(libs.androidx.navigation)

    // Local Storage
    implementation(libs.datastore.preferences)

    // ROOM
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    annotationProcessor(libs.room.compiler)

    // KSP
    ksp(libs.room.compiler)
    ksp(libs.hilt.compiler)

    // FIREBASE
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.google.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.appcheck.playintegrity)
    debugImplementation(libs.firebase.appcheck.debug)
    implementation(libs.play.services.auth)

    // GSON
    implementation(libs.gson)

    // HILT
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)

    // SPLASH SCREEN
    implementation(libs.androidx.core.splashscreen)
}