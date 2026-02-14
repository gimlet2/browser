# Rendering Engine Architecture

## Overview

This document describes the custom rendering engine built from scratch for the Kotlin-Native browser project. The rendering engine is designed as a pure Kotlin-Native library with zero dependencies, demonstrating browser rendering concepts in native code.

## Architecture

The rendering engine follows a modular pipeline architecture, similar to modern web browsers:

```
HTML/CSS Input → Parse → Style → Layout → Paint → Display Commands
```

### Components

#### 1. DOM (Document Object Model)
**File:** `src/commonMain/kotlin/com/gimlet2/browser/rendering/Dom.kt`

The DOM module provides the tree structure for representing HTML documents:

- **`DomNode`**: Base sealed class for all node types
- **`ElementNode`**: Represents HTML elements with tag names and attributes
- **`TextNode`**: Represents text content
- **`Document`**: Root container with helper methods for querying elements

Features:
- Element lookup by ID (`getElementById`)
- Element lookup by tag name (`getElementByTagName`)
- CSS class parsing
- Attribute management

#### 2. HTML Parser
**File:** `src/commonMain/kotlin/com/gimlet2/browser/rendering/HtmlParser.kt`

A custom HTML parser that converts HTML strings into DOM trees:

- Handles nested elements
- Parses attributes (quoted and unquoted)
- Supports self-closing tags (img, br, etc.)
- Whitespace handling
- Text node extraction

Features:
- Simple recursive descent parsing
- No external dependencies
- Multiplatform compatible

#### 3. CSS Parser and Data Structures
**File:** `src/commonMain/kotlin/com/gimlet2/browser/rendering/Css.kt`

CSS parsing and representation:

**Data Structures:**
- **`CssValue`**: Sealed class for CSS values (Keyword, Length, Color)
- **`CssUnit`**: Enum for length units (PX, EM, REM, PERCENT)
- **`Selector`**: CSS selectors (tag, id, class)
- **`Declaration`**: Property-value pairs
- **`Rule`**: Selector + declarations
- **`Stylesheet`**: Collection of rules

**Parser Features:**
- Tag selectors (`div`)
- ID selectors (`#main`)
- Class selectors (`.container`)
- Color parsing (hex colors like #ff0000)
- Length parsing with units
- Comment handling

#### 4. Layout Engine
**File:** `src/commonMain/kotlin/com/gimlet2/browser/rendering/Layout.kt`

Implements the CSS box model and layout calculations:

**Key Data Structures:**
- **`Rect`**: Position and dimensions
- **`EdgeSizes`**: Margins, borders, padding
- **`Dimensions`**: Complete box dimensions
- **`StyledNode`**: DOM node + matched CSS styles
- **`LayoutBox`**: Box in the layout tree

**Layout Process:**
1. Build styled tree by matching CSS rules to DOM
2. Create layout tree with box types (block, inline)
3. Calculate widths (based on container and CSS)
4. Calculate positions (relative to parent)
5. Layout children recursively
6. Calculate heights (based on content)

**Supported Features:**
- Block layout
- Box model (margin, border, padding, content)
- Width and height calculations
- Position calculations
- Display property (block, inline, none)

#### 5. Rendering/Paint Engine
**File:** `src/commonMain/kotlin/com/gimlet2/browser/rendering/Rendering.kt`

Converts layout boxes into display commands:

**Display Commands:**
- **`SolidColor`**: Fill rectangle with color (backgrounds)
- **`Text`**: Draw text at position
- **`Rectangle`**: Draw borders

**Process:**
1. Traverse layout tree
2. Generate background fills
3. Generate borders
4. Generate text rendering commands
5. Output display list

The display list is backend-agnostic and can be rendered by different graphics systems.

#### 6. Main Rendering Engine
**File:** `src/commonMain/kotlin/com/gimlet2/browser/rendering/BrowserRenderingEngine.kt`

Facade that coordinates all components:

```kotlin
val engine = BrowserRenderingEngine()
val displayList = engine.render(htmlContent, cssContent, 800f, 600f)
```

## Kotlin-Native Structure

The project uses Kotlin-Native with the following source sets:

```
src/
├── commonMain/         # Platform-agnostic rendering engine (shared across all native targets)
│   └── kotlin/
│       └── com/gimlet2/browser/rendering/
│           ├── Dom.kt
│           ├── HtmlParser.kt
│           ├── Css.kt
│           ├── Layout.kt
│           ├── Rendering.kt
│           └── BrowserRenderingEngine.kt
├── commonTest/         # Tests (shared across all native targets)
│   └── kotlin/
│       └── com/gimlet2/browser/rendering/
│           ├── HtmlParserTest.kt
│           ├── CssParserTest.kt
│           └── RenderingEngineTest.kt
├── nativeMain/         # Native-specific code
│   └── kotlin/
│       └── com/gimlet2/browser/
│           └── Main.kt         # Native entry point
└── nativeTest/         # Native-specific tests
    └── kotlin/
        └── com/gimlet2/browser/rendering/
```

## Native Target Support

The rendering engine compiles to native binaries for multiple platforms:

- **Linux**: `linuxX64`
- **macOS Intel**: `macosX64`
- **macOS Apple Silicon**: `macosArm64`
- **Windows**: `mingwX64`

### Building for Native Targets

```bash
# Build for specific platform
./gradlew linuxX64Binaries
./gradlew macosX64Binaries
./gradlew macosArm64Binaries
./gradlew mingwX64Binaries

# Run native binary
./gradlew linuxX64Binaries
./gradlew macosX64Binaries

# Run the binary
./build/bin/linuxX64/releaseExecutable/browser.kexe
./build/bin/macosX64/releaseExecutable/browser.kexe
```

Note: First-time native compilation requires downloading Kotlin-Native toolchains from `download.jetbrains.com`.

## Design Decisions

### Why Custom Rendering Engine?

1. **Educational**: Understanding browser internals from first principles
2. **Zero Dependencies**: All parsers and engines built from scratch
3. **Control**: Full control over rendering pipeline
4. **Lightweight**: No external dependencies, minimal code

### Why Kotlin-Native?

1. **Native Performance**: Compiles to machine code, no runtime overhead
2. **Small Binaries**: No JVM, smaller executables
3. **Cross-Platform**: Single codebase compiles to all native platforms
4. **Type Safety**: Kotlin's type system catches errors at compile time
5. **Modern Language**: Kotlin's safety and expressiveness

### Limitations

Current implementation is simplified for educational purposes:

- **Layout**: Only block layout (no flexbox, grid)
- **CSS**: Limited property support
- **JavaScript**: Not implemented
- **Images**: Not implemented
- **Fonts**: Basic text rendering only
- **Network**: No HTTP client

These can be extended as needed.

## Testing

All rendering engine components have comprehensive tests:

```bash
# Run all tests
./gradlew allTests

# Run tests for specific platform
./gradlew linuxX64Test
./gradlew macosX64Test
./gradlew mingwX64Test
```

## Usage Example

```kotlin
// Create rendering engine
val engine = BrowserRenderingEngine()

// HTML content
val html = """
    <div class="container">
        <h1>Hello World</h1>
        <p>This is a paragraph.</p>
    </div>
"""

// CSS styling
val css = """
    .container {
        background-color: #f0f0f0;
        padding: 20px;
        width: 600px;
    }
    h1 {
        color: #333;
        font-size: 24px;
    }
    p {
        color: #666;
        margin: 10px;
    }
"""

// Render with viewport size
val displayList = engine.render(html, css, 800f, 600f)

// Display commands can now be rendered by any graphics backend
for (command in displayList.commands) {
    when (command) {
        is DisplayCommand.SolidColor -> {
            // Fill rectangle with color
        }
        is DisplayCommand.Text -> {
            // Draw text
        }
        is DisplayCommand.Rectangle -> {
            // Draw border
        }
    }
}
```

## Future Enhancements

Possible improvements:

1. **Advanced Layout**: Flexbox, Grid, absolute positioning
2. **More CSS Properties**: Fonts, transforms, animations
3. **Image Support**: PNG, JPEG, SVG rendering
4. **JavaScript Engine**: V8 or QuickJS integration
5. **Incremental Rendering**: Only re-render changed parts
6. **GPU Acceleration**: Skia or native graphics integration
7. **Accessibility**: Screen reader support, keyboard navigation

## Performance

The rendering engine is designed for reasonable performance:

- **Parsing**: O(n) where n is input size
- **Styling**: O(elements × rules)
- **Layout**: O(elements) single-pass layout
- **Painting**: O(elements) for display list generation

For large documents, consider:
- Incremental parsing
- Style caching
- Layout caching
- Viewport culling

## Contributing

When modifying the rendering engine:

1. Keep code in `commonMain` platform-agnostic
2. Add tests for new features
3. Document non-obvious algorithms
4. Consider performance implications
5. Test on multiple platforms
