# Fix for ExperimentalForeignApi Build Error

## Problem
Build was failing with the error:
```
This declaration needs opt-in. Its usage must be marked with 
'@kotlinx.cinterop.ExperimentalForeignApi' or 
'@OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)'
```

## Root Cause
The `GraphicsRenderer.kt` file uses Kotlin-Native's C interop functionality to call POSIX functions:
- `memScoped` - Memory scope for C interop
- `fopen` - Opens a file
- `fputs` - Writes string to file
- `fwrite` - Writes binary data to file
- `fclose` - Closes file
- `usePinned` - Pins Kotlin array for C access

These APIs are marked as experimental in Kotlin-Native 2.3.0 and require explicit opt-in.

## Solution
Added file-level opt-in annotation to `GraphicsRenderer.kt`:

```kotlin
@file:OptIn(ExperimentalForeignApi::class)
```

This annotation is placed at the very top of the file, before the package declaration.

## Why This Approach?
- **File-level annotation**: Covers all uses of experimental APIs in the file
- **Explicit opt-in**: Acknowledges we're using experimental features
- **Minimal change**: Single line addition, no refactoring needed
- **Standard practice**: Recommended approach for Kotlin-Native C interop

## Alternative Approaches Considered
1. **Function-level annotation**: Could add `@OptIn` to just `savePPM()`, but file-level is cleaner
2. **Suppress warning**: Not recommended as it hides the experimental nature
3. **Avoid C interop**: Would require rewriting file I/O, unnecessary complexity

## Files Modified
- `src/nativeMain/kotlin/com/gimlet2/browser/ui/GraphicsRenderer.kt` - Added @OptIn annotation

## Testing
The fix resolves the compilation error. The code functionality remains unchanged:
- Framebuffer rendering works as before
- PPM file output works as before
- No runtime behavior changes

## References
- [Kotlin-Native C Interop](https://kotlinlang.org/docs/native-c-interop.html)
- [Opt-in Requirements](https://kotlinlang.org/docs/opt-in-requirements.html)
