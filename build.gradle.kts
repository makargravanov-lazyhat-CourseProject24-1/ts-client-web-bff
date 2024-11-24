
plugins {
    alias(libs.plugins.spring.kotlin)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
}

dependencies {
    implementation(rootProject.libs.spring.boot.starter)
    implementation(rootProject.libs.spring.webMVC)
    implementation(rootProject.libs.kotlin.reflect)
    implementation(rootProject.projects.tsBackendCommon)
}