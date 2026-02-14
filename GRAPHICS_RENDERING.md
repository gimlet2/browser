# Browser UI Rendering

## Overview

The browser now includes actual graphics rendering capabilities! The rendering engine generates display commands that are drawn to a framebuffer, creating actual visual output.

## Architecture

### Graphics Rendering Pipeline

```
HTML/CSS → Parsing → Layout → Display Commands → Framebuffer → Image Output
```

### Components

1. **Framebuffer** - Pixel buffer for rendering (RGB, 800x600)
2. **GraphicsRenderer** - Renders DisplayCommands to framebuffer
3. **Main Application** - Coordinates rendering and saves output

## Features

### Visual Rendering
- ✅ **Filled Rectangles**: Background colors and boxes
- ✅ **Borders**: Rectangle outlines with configurable width
- ✅ **Text**: Simple text rendering (basic bitmap font)
- ✅ **Colors**: Full RGB color support

### Browser UI Elements
The example renders a complete browser-like UI:
- **Browser Chrome**: Gray top bar (simulating browser toolbar)
- **URL Bar**: White input field with border
- **Content Area**: Main content with styled elements
- **Styled Boxes**: Colored containers with borders

## Output Format

The renderer outputs to PPM (Portable Pixel Map) format:
- Simple, no external dependencies
- Can be converted to PNG/JPEG with ImageMagick
- Direct pixel buffer output

## Usage

```bash
# Build and run
./gradlew linuxX64Binaries
./build/bin/linuxX64/releaseExecutable/browser.kexe

# Output
Browser UI rendered to: browser_output.ppm

# Convert to PNG (if ImageMagick is installed)
convert browser_output.ppm browser_output.png
```

## Example Output

The application renders this HTML:

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
```

With CSS styling:

```css
.browser-chrome {
    background-color: #e8e8e8;
    padding: 5px;
}
.url-bar {
    background-color: #ffffff;
    color: #333333;
    padding: 8px;
    border-color: #cccccc;
    border-width: 1px;
}
.content {
    background-color: #ffffff;
    padding: 20px;
}
/* ... more styles ... */
```

## Visual Layout

The rendered output shows:

```
┌─────────────────────────────────────────────────────┐
│ Gray Browser Chrome Bar (5px padding)              │
│ ┌─────────────────────────────────────────────────┐│
│ │ White URL Bar: https://example.com  (border)    ││
│ └─────────────────────────────────────────────────┘│
├─────────────────────────────────────────────────────┤
│                                                     │
│  White Content Area                                 │
│                                                     │
│  Welcome to Kotlin-Native Browser!                  │
│  This is rendered by the custom engine.             │
│                                                     │
│  ┌──────────────────────────────────────────────┐  │
│  │ Light Gray Box (with border)                  │  │
│  │ Features                                      │  │
│  │ Custom HTML/CSS rendering                     │  │
│  │ Native graphics output                        │  │
│  └──────────────────────────────────────────────┘  │
│                                                     │
└─────────────────────────────────────────────────────┘
```

## Technical Details

### Framebuffer Implementation
- RGB pixel array (3 bytes per pixel)
- 800x600 resolution
- Direct pixel manipulation
- No GPU/hardware acceleration (pure software rendering)

### Rendering Methods
- `fillRect()` - Draw filled rectangles (backgrounds)
- `drawRect()` - Draw rectangle borders
- `drawText()` - Render text (basic 8x12 bitmap font)
- `setPixel()` - Direct pixel access

### Performance
- Software rendering (CPU-based)
- Suitable for static page rendering
- ~800x600 = 480,000 pixels = 1.44MB buffer
- Rendering time: milliseconds for typical pages

## Future Enhancements

Possible improvements:
1. **Better Font Rendering**: Integrate TrueType font library
2. **Anti-aliasing**: Smooth edges and text
3. **Images**: PNG/JPEG decoding and display
4. **Interactive Mode**: Window with input handling
5. **GPU Acceleration**: Hardware-accelerated rendering
6. **Multiple Formats**: Support PNG, BMP, etc. output

## Comparison

**Before**: Console text output only
```
Generated 15 display commands:
  1. Fill rectangle (x=0, y=0, w=800, h=50) with color rgb(232, 232, 232)
  2. Draw text 'https://example.com' at (x=13, y=13)
  ...
```

**Now**: Actual visual rendering
- Framebuffer with real pixels
- Visual output file (PPM image)
- True graphical representation of HTML/CSS
- Browser-like UI layout

## Notes

- PPM format is uncompressed (large files)
- Use ImageMagick or similar tools to convert to PNG
- Software rendering is slower than GPU but works everywhere
- No external dependencies required (pure Kotlin-Native)
