plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.spring.kotlin)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
}
val springCloudVersion by extra("2024.0.0-RC1")

repositories {
    mavenCentral()
    maven("https://repo.spring.io/milestone/")
}

group = "ru.jetlabs"

dependencies {
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.webMVC)
    implementation(libs.lombok)
    implementation(libs.kotlin.reflect)
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("io.projectreactor:reactor-core:3.7.0")

}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}