plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

kapt {
    correctErrorTypes = true
}
android {
    namespace = "com.tuanvu.quanlichitieu"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tuanvu.quanlichitieu"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("com.airbnb.android:epoxy:5.1.4")
    implementation("com.airbnb.android:epoxy-databinding:5.1.4")
    kapt("com.airbnb.android:epoxy-processor:5.1.4")

    implementation ("androidx.multidex:multidex:2.0.1")

    // Layout error alignment library for horizontal epoxy model
    implementation("com.github.rubensousa:gravitysnaphelper:2.2.2")
    implementation ("com.google.android.flexbox:flexbox:3.0.0")

    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.fragment:fragment-ktx:1.7.0")

    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.intuit.ssp:ssp-android:1.1.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("com.airbnb.android:lottie:6.3.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation ("com.github.1902shubh:SendMail:1.0.0")
    implementation ("androidx.lifecycle:lifecycle-process:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    annotationProcessor ("androidx.lifecycle:lifecycle-compiler:2.6.2")

    //epoxy
    implementation("com.airbnb.android:epoxy:5.1.4")
    implementation("com.airbnb.android:epoxy-databinding:5.1.4")
    kapt("com.airbnb.android:epoxy-processor:5.1.4")

}