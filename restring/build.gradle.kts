import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.dokka")
    id("jacoco-configuration")
    id("maven-publish")
}

android {
    compileSdkVersion(AppConfig.compileSdk)

    defaultConfig {
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        buildTypes {
            getByName("debug") {
                isMinifyEnabled = false
            }
        }
        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            }
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}


fun getVersionName(): String {
    return "1.0.1"
}

fun getArtificatId(): String {
    return "restring"
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "dev.vkrasnousov"
                artifactId = getArtificatId()
                version = getVersionName()
            }
        }
        repositories {
            maven {
                url = uri(layout.buildDirectory.dir("repo"))
            }
        }
    }
}

dependencies {
    implementation(Dependencies.kotlin)
    implementation(Dependencies.appCompat)

    // Test libraries
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.kluent)
    testImplementation(Dependencies.robolectric)
    testImplementation(Dependencies.mockitoKotlin)
    testImplementation(Dependencies.mockitoInline)
    testImplementation(Dependencies.testCore)

    testImplementation(Dependencies.material)

    testImplementation(Dependencies.viewPump)
    testImplementation(Dependencies.reword)
}
