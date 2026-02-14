plugins {
    kotlin("multiplatform") version "2.3.0"
}

group = "com.gimlet2.browser"
version = "1.0.0"

repositories {
    mavenCentral()
}

// Detect platform for JavaFX
val osName = System.getProperty("os.name").lowercase()
val platform = when {
    osName.contains("linux") -> "linux"
    osName.contains("mac") || osName.contains("darwin") -> "mac"
    osName.contains("win") -> "win"
    else -> "linux"
}

kotlin {
    jvmToolchain(25)
    
    jvm()
    
    // Native targets for Kotlin-Native support
    // Note: First-time native compilation requires downloading toolchains from download.jetbrains.com
    // which requires network access. Uncomment when network access is available:
    
    // linuxX64()
    // macosX64()
    // macosArm64()
    // mingwX64()
    
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
                // Add JavaFX dependencies for detected platform
                implementation("org.openjfx:javafx-controls:23.0.1:$platform")
                implementation("org.openjfx:javafx-web:23.0.1:$platform")
                implementation("org.openjfx:javafx-graphics:23.0.1:$platform")
                implementation("org.openjfx:javafx-base:23.0.1:$platform")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.9.0")
            }
        }
        
        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
            }
        }
        
        // Native source sets - uncomment when native targets are enabled
        /*
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
        */
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
}

// Task to run the full browser application
tasks.register<JavaExec>("run") {
    group = "application"
    description = "Run the full browser application with JavaFX WebView"
    mainClass.set("com.gimlet2.browser.BrowserApplicationKt")
    classpath(kotlin.jvm().compilations["main"].output, configurations["jvmRuntimeClasspath"])
}

// Task to run the rendering engine demo
tasks.register<JavaExec>("runDemo") {
    group = "application"
    description = "Run the custom rendering engine demo"
    mainClass.set("com.gimlet2.browser.RenderingEngineDemoKt")
    classpath(kotlin.jvm().compilations["main"].output, configurations["jvmRuntimeClasspath"])
}
