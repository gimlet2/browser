# Example Browser UI Output

## Visual Representation

When you run the browser, it renders an HTML page to an image file. Here's what the output looks like:

```
┌────────────────────────────────────────────────────────────────────────┐
│                         Browser Window (800x600)                       │
├────────────────────────────────────────────────────────────────────────┤
│ ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ │  <- Gray Chrome Bar
│ ░░┌──────────────────────────────────────────────────────────────┐░░ │
│ ░░│  https://example.com                                         │░░ │  <- White URL Bar
│ ░░└──────────────────────────────────────────────────────────────┘░░ │
├────────────────────────────────────────────────────────────────────────┤
│                                                                        │
│   Welcome to Kotlin-Native Browser!                                   │  <- Dark Blue H1
│                                                                        │
│   This is rendered by the custom engine.                              │  <- Gray Text
│                                                                        │
│   ┌──────────────────────────────────────────────────────────────┐   │
│   │ ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ │   │  <- Light Gray Box
│   │ ░░                                                         ░░ │   │
│   │ ░░  Features                                               ░░ │   │  <- H2
│   │ ░░                                                         ░░ │   │
│   │ ░░  Custom HTML/CSS rendering                             ░░ │   │  <- Paragraphs
│   │ ░░  Native graphics output                                ░░ │   │
│   │ ░░                                                         ░░ │   │
│   │ ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ │   │
│   └──────────────────────────────────────────────────────────────┘   │
│                                                                        │
└────────────────────────────────────────────────────────────────────────┘
```

## Color Scheme

### Browser Chrome
- **Background**: #e8e8e8 (Light Gray)
- **Padding**: 5px

### URL Bar
- **Background**: #ffffff (White)
- **Text Color**: #333333 (Dark Gray)
- **Border**: #cccccc (Light Gray), 1px
- **Padding**: 8px

### Content Area
- **Background**: #ffffff (White)
- **Padding**: 20px

### Text Elements
- **H1**: #2c3e50 (Dark Blue)
- **H2**: #34495e (Medium Blue-Gray)
- **Paragraphs**: #555555 (Medium Gray)

### Feature Box
- **Background**: #ecf0f1 (Very Light Gray)
- **Border**: #bdc3c7 (Gray), 2px
- **Padding**: 15px

## File Output

### PPM Format
The output is saved as `browser_output.ppm`:
- **Resolution**: 800x600 pixels
- **Format**: RGB, 24-bit color
- **Size**: ~1.4 MB (uncompressed)

### File Structure
```
P6
800 600
255
[binary pixel data: R G B R G B R G B ...]
```

### Conversion Examples

```bash
# Convert to PNG (requires ImageMagick)
convert browser_output.ppm browser_output.png

# Convert to JPEG
convert browser_output.ppm -quality 90 browser_output.jpg

# Resize
convert browser_output.ppm -resize 400x300 browser_thumbnail.png

# View in terminal (if supported)
imgcat browser_output.ppm  # iTerm2
viu browser_output.ppm     # Generic terminal viewer
```

## Actual Pixels

Each element is rendered with real pixels:

### URL Bar Rectangle
```
x=5, y=5, width=790, height=30
Border: 1px, #cccccc
Fill: #ffffff
```

### H1 Text
```
Position: x=30, y=70
Color: #2c3e50
Text: "Welcome to Kotlin-Native Browser!"
Size: 8x12 per character (bitmap font)
```

### Feature Box
```
x=30, y=150, width=740, height=120
Border: 2px, #bdc3c7
Fill: #ecf0f1
```

## Display Commands Generated

Example of commands that create the visual output:

```
1. Fill rectangle (x=0, y=0, w=800, h=40) rgb(232, 232, 232)     # Chrome bar
2. Fill rectangle (x=5, y=5, w=790, h=30) rgb(255, 255, 255)     # URL bar background
3. Draw border (x=5, y=5, w=790, h=30) rgb(204, 204, 204) 1px    # URL bar border
4. Draw text 'https://example.com' at (x=13, y=13) rgb(51, 51, 51)
5. Fill rectangle (x=10, y=50, w=780, h=540) rgb(255, 255, 255)  # Content background
6. Draw text 'Welcome to Kotlin-Native Browser!' at (x=30, y=70)
7. Draw text 'This is rendered by the custom engine.' at (x=30, y=110)
8. Fill rectangle (x=30, y=150, w=740, h=120) rgb(236, 240, 241) # Box background
9. Draw border (x=30, y=150, w=740, h=120) rgb(189, 195, 199) 2px
10. Draw text 'Features' at (x=40, y=162)
... and more
```

## Rendering Process

1. **Parse HTML** → DOM tree
2. **Parse CSS** → Stylesheet
3. **Match styles** → Styled DOM
4. **Compute layout** → Box dimensions and positions
5. **Generate commands** → DisplayList
6. **Render to framebuffer** → Pixel array
7. **Save to file** → browser_output.ppm

## Future Improvements

### Planned Enhancements
- [ ] Anti-aliased text rendering
- [ ] TrueType font support
- [ ] PNG/JPEG direct output
- [ ] Image elements (img tag)
- [ ] Gradient backgrounds
- [ ] Box shadows
- [ ] Border radius
- [ ] Transforms and animations

### Interactive Mode
- [ ] Window system integration
- [ ] Mouse/keyboard input
- [ ] Real-time rendering
- [ ] URL navigation
- [ ] Scrolling support

## Comparison: Before vs After

### Before (Console Only)
```
Generated 15 display commands:
  1. Fill rectangle (x=0, y=0, w=800, h=50) with color rgb(232, 232, 232)
  2. Draw text 'https://example.com' at (x=13, y=13)
  ...
```
*No visual output, just text descriptions*

### After (Graphics Rendering)
```
Browser UI rendered to: browser_output.ppm
```
*Actual 800x600 pixel image with rendered HTML/CSS*

## Performance

Typical rendering performance:
- **Parsing**: <1ms (simple HTML/CSS)
- **Layout**: ~1-2ms (20-30 elements)
- **Display Commands**: <1ms
- **Pixel Rendering**: ~5-10ms (800x600)
- **File I/O**: ~50ms (PPM write)
- **Total**: ~60-70ms per page

## Viewing the Output

### Linux
```bash
# Using ImageMagick
display browser_output.ppm

# Using GIMP
gimp browser_output.ppm

# Using Eye of GNOME
eog browser_output.ppm
```

### macOS
```bash
# Using Preview (after conversion)
convert browser_output.ppm browser_output.png
open browser_output.png
```

### Windows
```bash
# Using default image viewer (after conversion)
magick browser_output.ppm browser_output.png
start browser_output.png
```
