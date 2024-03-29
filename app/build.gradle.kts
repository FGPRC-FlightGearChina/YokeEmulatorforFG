/*
 * Copyright 2021 CHH2000day,FGPRC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
}
android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "org.fgprc.nyanpasu.fgyoke"
        minSdk = 21
        targetSdk = 30
        versionCode = 6
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
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.fragment:fragment-ktx:1.3.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
}
