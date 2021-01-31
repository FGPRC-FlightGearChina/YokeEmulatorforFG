plugins {
    id("com.android.application")
    kotlin("android")
}
android {
    compileSdkVersion(30)
    buildToolsVersion = "30.0.2"

    defaultConfig {
        applicationId = "org.fgprc.nyanpasu.fgyoke"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode(6)
        versionName = "0.4.1 beta"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

@Suppress("SpellCheckingInspection")
dependencies {
//    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinVersion}")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
}
