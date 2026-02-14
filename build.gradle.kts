plugins {
    kotlin("jvm") version "1.9.22"
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "com.gimlet2.browser"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.openjfx:javafx-controls:21.0.1")
    implementation("org.openjfx:javafx-web:21.0.1")
    testImplementation(kotlin("test"))
}

javafx {
    version = "21.0.1"
    modules = listOf("javafx.controls", "javafx.web")
}

application {
    mainClass.set("com.gimlet2.browser.BrowserApplicationKt")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
