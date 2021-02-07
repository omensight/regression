plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

val kotlin_version = "1.4.10"

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")
    defaultConfig {
        applicationId = "com.alphemsoft.education.regression"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 30
        versionName = "3.0.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true

    }


    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFile(file("proguard-rules.pro"))

        }
    }

    buildFeatures {
        dataBinding = true
    }
    viewBinding.isEnabled = true
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }


    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    sourceSets{
        getByName("test"){
            val newList = mutableListOf<File>()
            newList.addAll(java.srcDirs)
            newList.add(file("$projectDir/src/testShared"))
            java.setSrcDirs(newList)

        }
        getByName("androidTest"){
            val newList = mutableListOf<File>()
            newList.addAll(java.srcDirs)
            newList.add(file("$projectDir/src/testShared"))
            java.setSrcDirs(newList)
        }
    }
    testOptions{
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    val dagger_version = "2.22.1"
    val room_version = "2.3.0-alpha03"
    val paging_version = "3.0.0-alpha04"
    val navigation_version = "2.3.1"
    val multidex_version = "2.0.1"
    val billing_version = "3.0.0"

    implementation(fileTree(mapOf("dir" to "libs", "include" to "*.jar")))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.activity:activity-ktx:1.2.0-rc01")
    implementation("com.google.android.gms:play-services-ads:19.5.0")

    testImplementation("junit:junit:4.13.1")
    testImplementation("androidx.test.ext:junit:1.1.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    testImplementation("com.google.truth:truth:1.1.2")
    testImplementation("com.google.truth.extensions:truth-java8-extension:1.1.2")
    androidTestImplementation("com.google.truth:truth:1.1.2")
    androidTestImplementation("com.google.truth.extensions:truth-java8-extension:1.1.2")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("org.robolectric:robolectric:4.4")


    testImplementation("org.powermock:powermock-api-mockito2:2.0.9")
    testImplementation("org.powermock:powermock-module-junit4:2.0.9")
    testImplementation("org.powermock:powermock-module-junit4-rule:2.0.9")
    testImplementation("org.powermock:powermock-classloading-xstream:2.0.9")

    //Room
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.room:room-guava:$room_version")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")


    implementation("androidx.paging:paging-runtime-ktx:$paging_version")

    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("com.google.android.material:material:1.3.0-alpha03")

    implementation("com.google.dagger:hilt-android:2.28-alpha")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha02")
    kapt("com.google.dagger:hilt-android-compiler:2.28-alpha")
    kapt("androidx.hilt:hilt-compiler:1.0.0-alpha02")

    implementation("androidx.navigation:navigation-fragment-ktx:$navigation_version")
    implementation("androidx.navigation:navigation-ui-ktx:$navigation_version")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$navigation_version")

    implementation(project(":android_math"))
    implementation("org.apache.commons:commons-math3:3.5")

    //Multidex
    implementation("androidx.multidex:multidex:$multidex_version")

    implementation("androidx.preference:preference-ktx:1.1.1")

    //MPAndroidChart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    api(project(":nativetemplates"))
    implementation("org.apache.commons:commons-csv:1.8")

    //Billing
    implementation("com.android.billingclient:billing-ktx:$billing_version")

    //POI Android
    implementation( "com.github.SUPERCILEX.poi-android:poi:3.17")
    implementation("com.github.SUPERCILEX.poi-android:proguard:3.17")
    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:26.4.0"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics-ktx")
}

val secretConfigFile = file ("$projectDir/secret_config.gradle")
if (secretConfigFile.exists()) {
    apply(from = file ("$projectDir/secret_config.gradle"))
}