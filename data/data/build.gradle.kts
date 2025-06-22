plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.deezer.exoapplication.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 23
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    testFixtures {
        enable = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    testImplementation(libs.junit)

    implementation(project(":data:database"))
    implementation(project(":utils"))

    testFixturesImplementation(project(":testUtils"))
}
