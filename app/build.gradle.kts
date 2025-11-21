plugins {
    alias(libs.plugins.android.application)
}
android {
    namespace = "com.example.truyengo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.truyengo"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    packaging {
        resources {
            pickFirsts += "META-INF/**"
        }
    }
}

configurations.all {
    exclude(group = "org.mongodb", module = "bson-record-codec")
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.bson)
    implementation(libs.lombok)
    annotationProcessor(libs.lombok)
    implementation(libs.jakarta.mail.api)
    implementation(libs.gson)
    implementation(libs.mongodb.driver.sync)
    implementation(libs.jbcrypt)
    implementation(libs.okhttp.v4100)
    implementation(libs.json)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

}