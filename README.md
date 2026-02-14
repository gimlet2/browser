# Kotlin Web Browser

A web browser built in Kotlin with a **custom rendering engine from scratch** and **Kotlin-Native support**, featuring HTTP/1.1, HTTP/2, HTML5 parsing, and CSS3 styling.

## Features

### Custom Rendering Engine
- **Built from Scratch**: Complete HTML/CSS rendering pipeline
- **Kotlin Multiplatform**: Works on JVM and Kotlin-Native
- **Platform-Independent**: No reliance on JavaFX WebView for rendering logic
- **Modular Design**: Separate parsing, styling, layout, and painting phases

### Browser Features
- **HTTP Support**: HTTP/1.1 and HTTP/2 via OkHttp client
- **HTML5 Parsing**: Custom HTML parser with DOM tree generation
- **CSS3 Styling**: CSS parser with selector matching and style computation
- **Layout Engine**: CSS box model implementation with block layout
- **Minimalistic UI**: Clean interface with URL input and navigation controls
- **Navigation**: Back, forward, and reload functionality

### Kotlin-Native Support
- **Native Targets**: Linux (x64), macOS (x64, ARM64), Windows (x64)
- **Shared Codebase**: Rendering engine works across all platforms
- **No JVM Required**: Can compile to native binaries

## Architecture

The browser consists of two main components:

1. **Rendering Engine** (Platform-independent, in `commonMain`):
   - HTML Parser → DOM Tree
   - CSS Parser → Stylesheets
   - Style Tree Builder → Matched styles
   - Layout Engine → Box positions/dimensions
   - Paint Engine → Display commands

2. **Browser Application** (JVM-specific, in `jvmMain`):
   - JavaFX UI for display
   - OkHttp for networking
   - Navigation and history management

See [RENDERING_ENGINE.md](RENDERING_ENGINE.md) for detailed architecture documentation.

## Requirements

- Java 25 or higher (for JVM target)
- Gradle 9.3+ (included via wrapper)
- Kotlin-Native toolchain (auto-downloaded for native builds)

## Building

### JVM Build
To build the JVM version with browser UI:

```bash
./gradlew build
```

### Native Builds
To build for native platforms:

```bash
# Linux
./gradlew linuxX64MainKlibrary

# macOS
./gradlew macosX64MainKlibrary
./gradlew macosArm64MainKlibrary

# Windows
./gradlew mingwX64MainKlibrary
```

Note: First native build requires downloading toolchains from download.jetbrains.com.

## Running

To run the browser (JVM):

```bash
./gradlew run
```

## Usage

1. Launch the browser using the run command
2. Enter a URL in the address bar (e.g., `https://example.com`)
3. Press Enter or click the navigation buttons to browse
4. Use the back (←), forward (→), and reload (⟳) buttons to navigate

## Testing

Run all tests:
```bash
./gradlew test
```

Run rendering engine tests only:
```bash
./gradlew test --tests "com.gimlet2.browser.rendering.*"
```

Run JVM-specific tests:
```bash
./gradlew jvmTest
```

## Project Structure

```
browser/
├── src/
│   ├── commonMain/kotlin/          # Platform-independent rendering engine
│   │   └── com/gimlet2/browser/rendering/
│   │       ├── Dom.kt              # DOM data structures
│   │       ├── HtmlParser.kt       # HTML parser
│   │       ├── Css.kt              # CSS parser and data structures
│   │       ├── Layout.kt           # Layout engine (box model)
│   │       ├── Rendering.kt        # Paint engine
│   │       └── BrowserRenderingEngine.kt  # Main facade
│   ├── commonTest/kotlin/          # Multiplatform tests
│   ├── jvmMain/kotlin/             # JVM browser application
│   │   └── com/gimlet2/browser/
│   │       └── BrowserApplication.kt
│   └── jvmTest/kotlin/             # JVM-specific tests
├── build.gradle.kts                # Kotlin Multiplatform configuration
├── RENDERING_ENGINE.md             # Detailed rendering engine docs
└── README.md                       # This file
```

## Rendering Engine Features

### HTML Parser
- Nested elements
- Attributes (quoted/unquoted)
- Self-closing tags
- Text nodes
- Element queries (by ID, tag, class)

### CSS Parser
- Tag selectors (`div`)
- ID selectors (`#main`)
- Class selectors (`.container`)
- Color values (hex: `#ff0000`)
- Length values with units (`10px`, `2em`)
- Multiple properties per rule

### Layout Engine
- CSS box model (margin, border, padding, content)
- Block layout
- Width/height calculation
- Position calculation
- Display property (block, inline, none)

### Limitations
Current implementation is a simplified version with:
- Block layout only (no flexbox/grid)
- Limited CSS property support
- No JavaScript execution
- No image rendering
- Basic text rendering

These can be extended as the project evolves.

## Technology Stack

- **Language**: Kotlin 2.3.0 (Multiplatform)
- **Build Tool**: Gradle 9.3.1 with Kotlin DSL
- **UI Framework**: JavaFX 23.0.1 (JVM only)
- **HTTP Client**: OkHttp 4.12.0 (JVM only)
- **JVM Target**: Java 25
- **Testing**: JUnit 5 (Jupiter) + kotlin-test

## Documentation

- [RENDERING_ENGINE.md](RENDERING_ENGINE.md) - Rendering engine architecture
- [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md) - Implementation details
- [QUICKSTART.md](QUICKSTART.md) - Quick start guide
- [UI_DOCUMENTATION.md](UI_DOCUMENTATION.md) - UI reference

## License

MIT License - See LICENSE file for details
