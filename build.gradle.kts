plugins {
    kotlin("multiplatform") version "2.3.0"
}

group = "com.gimlet2.browser"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(25)
    
    jvm()
    
    // Native targets for Kotlin-Native support
    // Note: First-time native compilation requires downloading toolchains from download.jetbrains.com
    // Uncomment the following lines when network access to download.jetbrains.com is available:
    
    linuxX64()
    macosX64()
    macosArm64()
    mingwX64()
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                // Common dependencies (multiplatform)
                // The rendering engine code is in commonMain and works across all platforms
            }
        }
        
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        
        val jvmMain by getting {
            dependencies {
                implementation("com.squareup.okhttp3:okhttp:4.12.0")
                // Add JavaFX dependencies explicitly for Linux platform
                implementation("org.openjfx:javafx-controls:23.0.1:linux")
                implementation("org.openjfx:javafx-web:23.0.1:linux")
                implementation("org.openjfx:javafx-graphics:23.0.1:linux")
                implementation("org.openjfx:javafx-base:23.0.1:linux")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.9.0")
            }
        }
        
        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
            }
        }
        
        // Native source sets - rendering engine is available for all native targets
        val nativeMain by creating {
            dependsOn(commonMain)
        }
        
        val linuxX64Main by getting {
            dependsOn(nativeMain)
        }
        
        val macosX64Main by getting {
            dependsOn(nativeMain)
        }
        
        val macosArm64Main by getting {
            dependsOn(nativeMain)
        }
        
        val mingwX64Main by getting {
            dependsOn(nativeMain)
        }
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
}

// Create a run task for the JVM application
tasks.register<JavaExec>("run") {
    group = "application"
    mainClass.set("com.gimlet2.browser.BrowserApplicationKt")
    classpath(kotlin.jvm().compilations["main"].output, configurations["jvmRuntimeClasspath"])
}
