# Quick Start Guide

## Prerequisites

Make sure you have Java 21 or higher installed. You can check your Java version with:

```bash
java -version
```

## Build and Run

### Option 1: Using Gradle Run

The simplest way to run the browser:

```bash
./gradlew run
```

### Option 2: Build and Run Distribution

Build the distribution and run it:

```bash
# Build the distribution
./gradlew installDist

# Run the browser
./build/install/browser/bin/browser
```

### Option 3: Create Zip Distribution

Create a portable zip file:

```bash
./gradlew distZip
```

The zip file will be created in `build/distributions/browser-1.0.0.zip`

## Usage

1. **Enter a URL**: Type a URL in the address bar (e.g., `example.com` or `https://www.google.com`)
2. **Navigate**: Press Enter to load the page
3. **Use Controls**:
   - **←** (Back): Go to the previous page
   - **→** (Forward): Go to the next page
   - **⟳** (Reload): Refresh the current page

## Testing URLs

Try these URLs to test the browser:
- `example.com` - Simple example page
- `https://www.google.com` - Google search
- `https://github.com` - GitHub (HTTP/2 support)
- `https://www.wikipedia.org` - Wikipedia

## Features

- **HTTP/2 Support**: Uses OkHttp 4.12.0 with HTTP/2 protocol support
- **HTML5 & CSS3**: JavaFX WebView engine provides full HTML5 and CSS3 rendering
- **JavaScript**: Full JavaScript support enabled
- **History**: Back/forward navigation with history
- **Auto-HTTPS**: Automatically adds `https://` if no protocol specified

## Development

### Run Tests

```bash
./gradlew test
```

### Build

```bash
./gradlew build
```

### Clean Build

```bash
./gradlew clean build
```

## Troubleshooting

### JavaFX Issues

If you encounter JavaFX issues, make sure you have Java 21+ with JavaFX support, or use a distribution that includes JavaFX.

### Display Issues

On Linux, if you get display-related errors, you may need to set the `DISPLAY` environment variable:

```bash
export DISPLAY=:0
./gradlew run
```

### Memory Issues

If the browser uses too much memory, you can adjust JVM settings in `gradle.properties`:

```properties
org.gradle.jvmargs=-Xmx1g
```
