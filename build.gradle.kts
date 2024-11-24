
plugins {
    alias(libs.plugins.spring.kotlin)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
}

dependencies {
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.webMVC)
    implementation(libs.kotlin.reflect)
    implementation(projects.tsBackendCommon)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}