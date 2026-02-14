# Kotlin Web Browser

A minimalistic web browser built in Kotlin with JavaFX, supporting HTTP/1.1, HTTP/2, HTML5 rendering, and CSS3.

## Features

- **HTTP Support**: HTTP/1.1 and HTTP/2 support via OkHttp client
- **HTML5 & CSS3**: Full HTML5 and CSS3 rendering using JavaFX WebView
- **Kotlin Coroutines**: Asynchronous operations using Kotlin coroutines
- **Minimalistic UI**: Clean interface with URL input and navigation controls
- **Navigation**: Back, forward, and reload functionality
- **Cross-platform**: Runs on Windows, macOS, and Linux

## Requirements

- Java 25 or higher
- Gradle 8.5+ (included via wrapper)

## Building

To build the project:

```bash
./gradlew build
```

## Running

To run the browser:

```bash
./gradlew run
```

Or use the distribution:

```bash
./gradlew installDist
./build/install/browser/bin/browser
```

## Usage

1. Launch the browser using the run command
2. Enter a URL in the address bar (e.g., `https://example.com`)
3. Press Enter or click the navigation buttons to browse
4. Use the back (←), forward (→), and reload (⟳) buttons to navigate

## Architecture

- **BrowserApplication.kt**: Main application class with JavaFX UI and Kotlin coroutines
- **Kotlin Coroutines**: Asynchronous operations with kotlinx-coroutines-javafx
- **OkHttp**: HTTP client with HTTP/2 support
- **JavaFX WebView**: HTML5/CSS3 rendering engine
- **Gradle**: Build system with Kotlin DSL

## License

MIT License - See LICENSE file for details