# Kotlin-Native Browser Rendering Engine

A browser rendering engine built from scratch in **pure Kotlin-Native**, demonstrating HTML/CSS parsing, layout computation, and **interactive window display** without JVM dependencies.

## Features

### Custom Rendering Engine
- **Built from Scratch**: Complete HTML/CSS rendering pipeline
- **Pure Kotlin-Native**: No JVM, runs as native binary
- **Zero Dependencies**: Custom parsers, no external libraries (only SDL2 for window)
- **Cross-Platform**: Compiles to native binaries for Linux, macOS, Windows
- **✨ Interactive Window**: SDL2-based window with real-time display

### Rendering Pipeline
- **HTML Parser**: Recursive descent parser generating DOM trees
- **CSS Parser**: Selector matching (tag/id/class), property parsing  
- **Layout Engine**: CSS box model with width/height/position calculation
- **Paint Engine**: Display command generation for rendering backends
- **Graphics Renderer**: Framebuffer-based pixel rendering with visual output
- **Window Display**: SDL2 window showing rendered content

### Browser UI
- **Interactive Window**: Native window (800x600) displaying rendered HTML/CSS
- **Browser Chrome**: Gray toolbar at top
- **URL Bar**: White bar showing current URL
- **Styled Content**: Full CSS styling with colors, borders, padding
- **Text Rendering**: Basic text display (8x12 bitmap font)
- **Keyboard Controls**: ESC or Q to close window

## Architecture

The rendering engine implements a complete browser rendering pipeline with graphics output:

```
HTML/CSS Input → Parse → Style → Layout → Paint → Display Commands → Graphics → SDL2 Window
```

### Components

1. **DOM Parser** - Converts HTML to DOM tree
2. **CSS Parser** - Parses stylesheets and selectors
3. **Style Tree** - Matches CSS rules to DOM elements
4. **Layout Engine** - Computes box positions and dimensions
5. **Paint Engine** - Generates rendering commands
6. **Graphics Renderer** - Renders to framebuffer
7. **Window Display** - SDL2 window showing rendered content

See [RENDERING_ENGINE.md](RENDERING_ENGINE.md) for detailed architecture.
See [GRAPHICS_RENDERING.md](GRAPHICS_RENDERING.md) for graphics implementation details.
See [SDL2_WINDOW.md](SDL2_WINDOW.md) for window display documentation.

## Requirements

- Kotlin 2.3.0 or higher
- Gradle 9.3+ (included via wrapper)
- Kotlin-Native toolchain (auto-downloaded on first build)
- **SDL2 library** (for window display)

### Installing SDL2

**Linux (Ubuntu/Debian):**
```bash
sudo apt-get install libsdl2-dev
```

**macOS:**
```bash
brew install sdl2
```

**Windows:**
- Download from [libsdl.org](https://www.libsdl.org/)
- Or use MSYS2: `pacman -S mingw-w64-x86_64-SDL2`

## Building

### Compile Native Binary

```bash
# Linux
./gradlew linuxX64Binaries

# macOS (Intel)
./gradlew macosX64Binaries

# macOS (Apple Silicon)
./gradlew macosArm64Binaries

# Windows
./gradlew mingwX64Binaries
```

### Run Native Binary

```bash
# Linux
./build/bin/linuxX64/releaseExecutable/browser.kexe

# macOS
./build/bin/macosX64/releaseExecutable/browser.kexe

# Windows
./build/bin/mingwX64/releaseExecutable/browser.exe
```

**A window will open displaying the rendered browser UI!**
- Press **ESC** or **Q** to close the window
- Or click the window's close button

## Demo Output

The native application opens an interactive SDL2 window:

```
============================================================
Kotlin-Native Browser with Window UI
============================================================

Rendering HTML page with browser UI...
Generated 25 display commands

============================================================
Opening browser window...
Press ESC or Q to close, or close the window.
============================================================

SDL2 window initialized successfully
Window opened. Press ESC or Q to close, or close the window.
```

### Window Display

An 800x600 window opens showing:
- **Browser Chrome**: Gray toolbar at the top
- **URL Bar**: White input field showing "https://example.com"
- **Content Area**: White background with styled HTML content
- **Heading**: "Welcome to Kotlin-Native Browser!" in dark blue
- **Paragraphs**: Descriptive text in gray
- **Styled Box**: Light gray box with borders containing features

### Output Formats

**PPM Image** (Primary output):
```bash
browser_output.ppm  # 800x600 RGB image
```

**Convert to PNG** (if ImageMagick installed):
```bash
convert browser_output.ppm browser_output.png
```

### Example HTML Rendered

```html
<div class="browser-chrome">
    <div class="url-bar">https://example.com</div>
</div>
<div class="content">
    <h1>Welcome to Kotlin-Native Browser!</h1>
    <p>This is rendered by the custom engine.</p>
    <div class="box">
        <h2>Features</h2>
        <p>Custom HTML/CSS rendering</p>
        <p>Native graphics output</p>
    </div>
</div>
Kotlin-Native Browser Rendering Engine
============================================================

Example 1: Simple HTML
------------------------------------------------------------
HTML:
<div>
    <h1>Hello from Kotlin-Native!</h1>
    <p>This is rendered by the custom engine.</p>
</div>

CSS:
div {
    background-color: #f0f0f0;
    padding: 20px;
}

Generated 5 display commands:
  1. Fill rectangle (x=0.0, y=0.0, w=800.0, h=150.0) with color rgb(240, 240, 240)
  2. Draw text 'Hello from Kotlin-Native!' at (x=20.0, y=20.0)
  3. Draw text 'This is rendered by the custom engine.' at (x=20.0, y=50.0)
  ...
```

## Testing

Run native tests:

```bash
# Linux
./gradlew linuxX64Test

# macOS
./gradlew macosX64Test

# Windows
./gradlew mingwX64Test
```

## Project Structure

```
browser/
├── src/
│   ├── commonMain/kotlin/          # Platform-independent rendering engine
│   │   └── com/gimlet2/browser/rendering/
│   │       ├── Dom.kt              # DOM data structures
│   │       ├── HtmlParser.kt       # HTML parser
│   │       ├── Css.kt              # CSS parser
│   │       ├── Layout.kt           # Layout engine
│   │       ├── Rendering.kt        # Paint engine
│   │       └── BrowserRenderingEngine.kt
│   ├── commonTest/kotlin/          # Tests
│   ├── nativeMain/kotlin/          # Native-specific code
│   │   └── com/gimlet2/browser/
│   │       └── Main.kt             # Native entry point
│   └── nativeTest/kotlin/          # Native tests
├── build.gradle.kts                # Kotlin-Native build config
└── README.md                       # This file
```

## Technology Stack

- **Language**: Kotlin 2.3.0
- **Runtime**: Kotlin-Native (no JVM)
- **Build Tool**: Gradle 9.3.1
- **Targets**: Linux x64, macOS x64/ARM64, Windows x64
- **Dependencies**: None (all parsers/engines built from scratch)

## Rendering Engine Features

### HTML Parser
✅ Nested elements
✅ Attributes (quoted/unquoted)
✅ Self-closing tags
✅ Text nodes
✅ Element queries (by ID, tag, class)

### CSS Parser  
✅ Tag selectors (`div`)
✅ ID selectors (`#main`)
✅ Class selectors (`.container`)
✅ Color values (hex: `#ff0000`)
✅ Length values (`10px`, `2em`)
✅ Multiple properties per rule

### Layout Engine
✅ CSS box model (margin, border, padding, content)
✅ Block layout
✅ Width/height calculation
✅ Position calculation
✅ Display property (block, inline, none)

### Current Limitations

This is a simplified rendering engine for educational purposes:
- Block layout only (no flexbox/grid)
- Limited CSS property support
- No JavaScript execution
- No image rendering
- No network stack

These can be extended as needed.

## Performance

The rendering engine is designed for reasonable performance:

- **Parsing**: O(n) where n is input size
- **Styling**: O(elements × rules)
- **Layout**: O(elements) single-pass layout
- **Painting**: O(elements) for display list generation

## Why Kotlin-Native?

1. **Native Performance**: Compiles to machine code, no JVM overhead
2. **Small Binaries**: No runtime, smaller executables
3. **Cross-Platform**: Single codebase for all native platforms
4. **Memory Efficient**: Direct memory management
5. **Modern Language**: Kotlin's safety and expressiveness

## Documentation

- [RENDERING_ENGINE.md](RENDERING_ENGINE.md) - Rendering engine architecture
- [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) - Implementation details

## License

MIT License - See LICENSE file for details
