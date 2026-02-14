plugins {
    kotlin("multiplatform") version "2.3.0"
}

group = "com.gimlet2.browser"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    // Native targets for Kotlin-Native only
    linuxX64 {
        binaries {
            executable {
                entryPoint = "com.gimlet2.browser.main"
            }
        }
    }
    
    macosX64 {
        binaries {
            executable {
                entryPoint = "com.gimlet2.browser.main"
            }
        }
    }
    
    macosArm64 {
        binaries {
            executable {
                entryPoint = "com.gimlet2.browser.main"
            }
        }
    }
    
    mingwX64 {
        binaries {
            executable {
                entryPoint = "com.gimlet2.browser.main"
            }
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                // No dependencies needed for the rendering engine
            }
        }
        
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        
        val nativeMain by creating {
            dependsOn(commonMain)
        }
        
        val nativeTest by creating {
            dependsOn(commonTest)
        }
        
        val linuxX64Main by getting {
            dependsOn(nativeMain)
        }
        
        val linuxX64Test by getting {
            dependsOn(nativeTest)
        }
        
        val macosX64Main by getting {
            dependsOn(nativeMain)
        }
        
        val macosX64Test by getting {
            dependsOn(nativeTest)
        }
        
        val macosArm64Main by getting {
            dependsOn(nativeMain)
        }
        
        val macosArm64Test by getting {
            dependsOn(nativeTest)
        }
        
        val mingwX64Main by getting {
            dependsOn(nativeMain)
        }
        
        val mingwX64Test by getting {
            dependsOn(nativeTest)
        }
    }
}
