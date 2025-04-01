plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.text"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.text"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Retrofit 核心库
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Retrofit 的 Gson 转换器（用于 JSON 解析）
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp 核心库（Retrofit 底层依赖）
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    // OkHttp 日志拦截器（可选，用于调试）
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
