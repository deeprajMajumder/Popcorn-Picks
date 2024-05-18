// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false
    id("com.google.dagger.hilt.android") version "2.49" apply false
}
buildscript{
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Add the dependency for the Google services Gradle plugin
        classpath (libs.google.services)
        classpath (libs.hilt.android.gradle.plugin)
    }

}
tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
