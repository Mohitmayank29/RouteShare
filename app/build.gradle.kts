 plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id ("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.mohit.mapsone"
    compileSdk = 37


    defaultConfig {
        applicationId = "com.mohit.mapsone"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    //map
    implementation("com.google.android.gms:play-services-maps:19.2.0")
    implementation("com.google.android.gms:play-services-location:21.4.0")
    // Google Maps Compose
    implementation("com.google.maps.android:maps-compose:8.4.0")
    //location
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")
    // Core Material 3 components
    implementation("androidx.compose.material3:material3:1.4.0")
    implementation("androidx.compose.material3:material3-window-size-class:1.3.1")
    implementation("androidx.compose.material3.adaptive:adaptive:1.2.0")
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:34.16.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")
    //firestore
    implementation("com.google.firebase:firebase-firestore")
    //preference datatstore
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.security:security-crypto:1.1.0")
    implementation("androidx.datastore:datastore-preferences:1.2.1")
    //hiltviewmodel
    implementation ("com.google.dagger:hilt-android:2.60.1")
    ksp("com.google.dagger:hilt-compiler:2.60.1")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")
    //splashscreen
    implementation("androidx.core:core-splashscreen:1.0.1")
    //gogole ,etrail
    implementation("com.google.android.material:material:1.12.0")
    // Material Icons Extended (Needed for Email, Lock, Visibility, VisibilityOff, etc.)
    implementation("androidx.compose.material:material-icons-extended")
    // Polyline decoding / Geometry support (PolyUtil ke liye)
    implementation("com.google.maps.android:android-maps-utils:3.8.0")
    implementation("androidx.navigation:navigation-compose:2.8.5")

}
