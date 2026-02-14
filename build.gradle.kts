plugins {
    kotlin("jvm") version "2.1.0"
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
    implementation("org.openjfx:javafx-controls:23.0.1")
    implementation("org.openjfx:javafx-web:23.0.1")
    testImplementation(kotlin("test"))
}

javafx {
    version = "23.0.1"
    modules = listOf("javafx.controls", "javafx.web")
}

application {
    mainClass.set("com.gimlet2.browser.BrowserApplicationKt")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
