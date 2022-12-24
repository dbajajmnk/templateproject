plugins {
    id(Libs.Plugins.ANDROID_APP)
    id(Libs.Plugins.KOTLIN_ANDROID)
}

android {
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        applicationId = Versions.APP_ID
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = Versions.VERSION_CODE
        versionName = Versions.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled  = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = Versions.JVM_TARGET
    }
}

dependencies {
    implementation(Libs.KTS_CORE_LIB)
    implementation(Libs.APP_COMPAT_LIB)
    implementation(Libs.MATERIAL_LIB)
    implementation(Libs.CONSTRAINT_LAYOUT_LIB)
    testImplementation(Libs.JUNIT_LIB)
    androidTestImplementation(Libs.JUNIT_EXT_LIB)
    androidTestImplementation (Libs.ESPRESSO_CORE)
}