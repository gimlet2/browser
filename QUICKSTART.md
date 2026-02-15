# Quick Start Guide - Kotlin-Native Browser Rendering Engine

## Prerequisites

- Kotlin 2.3.0 or higher (included via Gradle wrapper)
- Gradle 9.3+ (included via wrapper)
- No JVM required!

## Build Native Binary

### Option 1: Build for Your Platform

```bash
# Determine your platform first
uname -s  # Linux, Darwin (macOS), or MINGW (Windows)

# Build for Linux
./gradlew linuxX64Binaries

# Build for macOS Intel
./gradlew macosX64Binaries

# Build for macOS Apple Silicon
./gradlew macosArm64Binaries

# Build for Windows
./gradlew mingwX64Binaries
```

### Option 2: Build All Targets

```bash
./gradlew allBinaries
```

## Run the Native Application

After building, run the native binary:

```bash
# Linux
./build/bin/linuxX64/releaseExecutable/browser.kexe

# macOS Intel
./build/bin/macosX64/releaseExecutable/browser.kexe

# macOS Apple Silicon
./build/bin/macosArm64/releaseExecutable/browser.kexe

# Windows
./build/bin/mingwX64/releaseExecutable/browser.exe
```

## What You'll See

The application demonstrates the custom rendering engine with three examples:

```
============================================================
Kotlin-Native Browser Rendering Engine
============================================================

Example 1: Simple HTML
------------------------------------------------------------
HTML:
<div>
    <h1>Hello from Kotlin-Native!</h1>
    <p>This is rendered by the custom engine.</p>
</div>

Generated 5 display commands:
  1. Fill rectangle (x=0.0, y=0.0, w=800.0, h=150.0) ...
  2. Draw text 'Hello from Kotlin-Native!' at (x=20.0, y=20.0)
  ...
```

## Features

- **Pure Native**: No JVM, compiles to native machine code
- **Zero Dependencies**: All HTML/CSS parsing built from scratch
- **Cross-Platform**: Single codebase for Linux, macOS, Windows
- **Small Binaries**: No runtime dependencies
- **Fast**: Native performance

## Development

### Run Tests

```bash
# Run all tests
./gradlew allTests

# Run tests for specific platform
./gradlew linuxX64Test
./gradlew macosX64Test
./gradlew macosArm64Test
./gradlew mingwX64Test
```

### Clean Build

```bash
./gradlew clean
./gradlew linuxX64Binaries
```

### Debug Build

By default, release binaries are built. For debug builds:

```bash
./gradlew linuxX64DebugBinaries
./build/bin/linuxX64/debugExecutable/browser.kexe
```

## Troubleshooting

### Kotlin-Native Toolchain Download

First-time compilation downloads Kotlin-Native toolchains from `download.jetbrains.com`. This requires:
- Internet connection
- ~500MB disk space
- 5-10 minutes for initial download

If download fails:
1. Check internet connection
2. Check firewall settings
3. Try again later (servers may be temporarily unavailable)

### Build Errors

If you encounter build errors:

```bash
# Clean everything
./gradlew clean

# Clear Gradle cache
rm -rf ~/.gradle/caches/

# Try again
./gradlew linuxX64Binaries
```

### Platform-Specific Issues

**Linux**: Ensure you have `gcc` and `g++` installed
```bash
sudo apt-get install build-essential
```

**macOS**: Install Xcode Command Line Tools
```bash
xcode-select --install
```

**Windows**: Install MinGW-w64 or use MSYS2

## Performance

Native binaries are:
- **Fast**: Compiled to machine code
- **Small**: ~2-5 MB (depending on platform)
- **Efficient**: No JVM overhead
- **Portable**: Single executable file

## What's Included

The rendering engine demonstrates:

1. **HTML Parser**: Converts HTML strings to DOM trees
2. **CSS Parser**: Parses selectors and properties
3. **Layout Engine**: Computes CSS box model positions/sizes
4. **Paint Engine**: Generates display commands

See [RENDERING_ENGINE.md](RENDERING_ENGINE.md) for architecture details.

## Next Steps

- Explore the source code in `src/commonMain/kotlin/`
- Modify examples in `src/nativeMain/kotlin/Main.kt`
- Run tests to see how components work
- Read [RENDERING_ENGINE.md](RENDERING_ENGINE.md) for architecture details
