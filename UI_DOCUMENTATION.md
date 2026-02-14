# User Interface Documentation

## Browser Window Layout

The browser window is organized as follows:

```
┌────────────────────────────────────────────────────────────────┐
│ Kotlin Browser                                            ╳  □  ─ │
├────────────────────────────────────────────────────────────────┤
│  ┌──┐ ┌──┐ ┌──┐ ┌──────────────────────────────────────────┐  │
│  │ ← │ │ → │ │ ⟳ │ │ Enter URL (e.g., https://example.com) │  │
│  └──┘ └──┘ └──┘ └──────────────────────────────────────────┘  │
├────────────────────────────────────────────────────────────────┤
│                                                                │
│                                                                │
│                   Web Content Area                             │
│                   (HTML5/CSS3 Rendering)                       │
│                                                                │
│                                                                │
│                                                                │
│                                                                │
│                                                                │
│                                                                │
│                                                                │
│                                                                │
└────────────────────────────────────────────────────────────────┘
```

## UI Components

### Navigation Bar (Top)
- **Back Button (←)**: Navigate to the previous page in history
  - Disabled when at the beginning of history
  - Width: 40px
  
- **Forward Button (→)**: Navigate to the next page in history
  - Disabled when at the end of history
  - Width: 40px
  
- **Reload Button (⟳)**: Refresh the current page
  - Always enabled when a page is loaded
  - Width: 40px
  
- **URL Input Field**: Text field for entering web addresses
  - Placeholder: "Enter URL (e.g., https://example.com)"
  - Auto-expands to fill available space
  - Press Enter to navigate
  - Automatically updates with current URL

### Web Content Area (Center)
- **WebView**: Full HTML5/CSS3 rendering engine
  - Supports modern web standards
  - JavaScript enabled
  - Responsive layout
  - Scrollable content

## Window Properties

- **Title**: "Kotlin Browser"
- **Default Size**: 1024 x 768 pixels
- **Resizable**: Yes
- **Layout**: BorderPane
  - Top: Navigation bar
  - Center: Web content

## Interaction Flow

1. **Starting the Browser**
   ```
   User launches → Window opens → Default URL loaded (example.com)
   ```

2. **Navigating to a URL**
   ```
   User types URL → Presses Enter → Page loads → URL field updates
   ```

3. **Using Back/Forward**
   ```
   User clicks back → Previous page loads → Buttons update state
   ```

4. **Reloading**
   ```
   User clicks reload → Current page refreshes
   ```

## Color Scheme

- **Background**: Default JavaFX colors (light theme)
- **Buttons**: Standard JavaFX button styling
- **URL Field**: White background with light border
- **Navigation Bar**: Light gray background with 5px spacing

## Accessibility

- All controls are keyboard accessible
- Tab navigation supported
- Enter key activates URL navigation
- Standard system fonts used
- Buttons have clear visual states (enabled/disabled)

## Responsive Design

- URL field automatically expands to fill available width
- Web content area scales with window size
- Navigation buttons maintain fixed width for consistency
- Minimum practical window size: 400 x 300 pixels

## Error Handling UI

When a page fails to load, an error page is displayed with:
- Error title
- Failed URL
- Error message details

```
┌────────────────────────────────────────┐
│  Error loading page                    │
│                                        │
│  Could not load: https://invalid.url  │
│  Error: Connection refused             │
└────────────────────────────────────────┘
```

## Example Screenshots (Text Representation)

### Browser Loading Google
```
┌────────────────────────────────────────────────────────────────┐
│ Kotlin Browser                                                  │
├────────────────────────────────────────────────────────────────┤
│  [←] [→] [⟳] [https://www.google.com                        ]  │
├────────────────────────────────────────────────────────────────┤
│                                                                │
│                        Google                                  │
│                  ┌──────────────────┐                         │
│                  │                  │ [Google Search]          │
│                  └──────────────────┘                         │
│                                                                │
└────────────────────────────────────────────────────────────────┘
```

### Browser with Navigation History
```
┌────────────────────────────────────────────────────────────────┐
│ Kotlin Browser                                                  │
├────────────────────────────────────────────────────────────────┤
│  [←] [→] [⟳] [https://github.com                            ]  │
│   ^   ^                                                        │
│   │   └── Enabled (can go forward)                            │
│   └────── Enabled (can go back)                               │
├────────────────────────────────────────────────────────────────┤
│                GitHub Page Content                             │
└────────────────────────────────────────────────────────────────┘
```

## Technical Details

- **Framework**: JavaFX 21.0.1
- **Rendering Engine**: WebKit (via JavaFX WebView)
- **Layout Manager**: BorderPane with HBox for navigation
- **Padding**: 5px between navigation elements
- **URL Field Behavior**: HBox.setHgrow with Priority.ALWAYS

## Browser Capabilities

✅ HTML5 support
✅ CSS3 support
✅ JavaScript execution
✅ HTTPS/HTTP protocols
✅ HTTP/2 protocol (via underlying OkHttp)
✅ Cookies
✅ Local storage
✅ Session storage
✅ Media playback (audio/video)
✅ Modern web APIs
