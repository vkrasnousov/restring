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

val githubProperties = Properties()
githubProperties.load(FileInputStream(rootProject.file("github.properties")))

fun getVersionName(): String {
    return "1.0.1"
}

fun getArtificatId(): String {
    return "restring"
}

publishing {
    publications {
        create<MavenPublication>("gpr") {
            run {
                groupId = "dev.vkrasnousov.libs"
                artifactId = getArtificatId()
                version = getVersionName()
                artifact("$buildDir/outputs/aar/${getArtificatId()}-release.aar")
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/vkrasnousov/${getArtificatId()}")
            credentials {
                username = githubProperties.get("gpr.usr") as String?
                        ?: System.getenv("GPR_USER")
                password =
                        githubProperties.get("gpr.key") as String?
                                ?: System.getenv("GPR_API_KEY")
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
