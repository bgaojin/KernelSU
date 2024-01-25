import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    alias(libs.plugins.agp.app)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.lsplugin.apksign)
    id("kotlin-parcelize")
}

val managerVersionCode: Int by rootProject.extra
val managerVersionName: String by rootProject.extra

//apksign {
//    storeFileProperty = "KEYSTORE_FILE"
//    storePasswordProperty = "KEYSTORE_PASSWORD"
//    keyAliasProperty = "KEY_ALIAS"
//    keyPasswordProperty = "KEY_PASSWORD"
//}

android {
    namespace = "me.weishu.kernelsu"
    signingConfigs {
        create("release") {
       //     storeFile =
   //             file("../singer/manger.jks")
     //       storePassword = "android"
     //       keyAlias = "ghapp"
    //        keyPassword = "android"

           storeFile =
               file("../singer/ghost.jks")
           storePassword = "android"
           keyAlias = "ghost"
           keyPassword = "android"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs["release"]
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        aidl = true
        buildConfig = true
        compose = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
        }
    }

    applicationVariants.all {
        outputs.forEach {
            val output = it as BaseVariantOutputImpl
            output.outputFileName = "KernelSU_${managerVersionName}_${managerVersionCode}-$name.apk"
        }

        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
    viewBinding{
        enable = true
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.com.google.accompanist.drawablepainter)
    implementation(libs.com.google.accompanist.navigation.animation)
    implementation(libs.com.google.accompanist.systemuicontroller)

    implementation(libs.compose.destinations.animations.core)
    ksp(libs.compose.destinations.ksp)

    implementation(libs.com.github.topjohnwu.libsu.core)
    implementation(libs.com.github.topjohnwu.libsu.service)

    implementation(libs.dev.rikka.rikkax.parcelablelist)

    implementation(libs.io.coil.kt.coil.compose)

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.me.zhanghai.android.appiconloader.coil)

    implementation(libs.sheet.compose.dialogs.core)
    implementation(libs.sheet.compose.dialogs.list)
    implementation(libs.sheet.compose.dialogs.input)

    implementation(libs.markdown)

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation ("io.reactivex.rxjava2:rxandroid:2.0.1")
    implementation ("io.reactivex.rxjava2:rxjava:2.1.8")
    implementation ("com.squareup.retrofit2:retrofit:2.4.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.4.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.4.0")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:2.4.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation( "com.jakewharton.rxrelay2:rxrelay:2.1.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation ("com.squareup.okhttp:okhttp-urlconnection:2.2.0")

    implementation ("org.greenrobot:eventbus:3.2.0")
    implementation ("com.tencent.bugly:crashreport:latest.release")
}
