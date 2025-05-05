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
        vectorDrawables.useSupportLibrary = true
    }
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        debug {
            buildConfigField("String", "API_BASE_URL", "\"http://10.29.61.159:5050/\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "API_BASE_URL", "\"http://10.29.61.159:5050/\"")
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

//    implementation("androidx.recyclerview:recyclerview:1.2.1") // 列表

    // Retrofit 核心库
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Retrofit 的 Gson 转换器（用于 JSON 解析）
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.8.9") // Gson 示例

    // OkHttp 核心库（Retrofit 底层依赖）
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    // OkHttp 日志拦截器（可选，用于调试）
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    implementation("com.github.bumptech.glide:glide:4.16.0") // 使用最新版本
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0") // 如果需要注解处理

//    implementation ("org.json:json:20230227")


    // 使用TinyPinyin库处理中文转拼音
    implementation("com.github.promeg:tinypinyin:2.0.3")
//    implementation 'com.github.promeg:tinypinyin:2.0.3'

    implementation("com.github.bumptech.glide:glide:4.16.0") // 使用最新版本
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0") // 如果需要注解处理

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
