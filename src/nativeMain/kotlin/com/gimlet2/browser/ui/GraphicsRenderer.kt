@file:OptIn(ExperimentalForeignApi::class)

package com.gimlet2.browser.ui

import com.gimlet2.browser.rendering.*
import kotlinx.cinterop.*
import platform.posix.*

/**
 * Simple framebuffer-based renderer
 * Renders display commands to a pixel buffer that can be output as an image
 */
class Framebuffer(val width: Int, val height: Int) {
    // RGB pixel buffer (3 bytes per pixel)
    private val pixels = ByteArray(width * height * 3)
    
    init {
        // Initialize to white background
        fill(255, 255, 255)
    }
    
    fun fill(r: Int, g: Int, b: Int) {
        for (i in pixels.indices step 3) {
            pixels[i] = r.toByte()
            pixels[i + 1] = g.toByte()
            pixels[i + 2] = b.toByte()
        }
    }
    
    fun setPixel(x: Int, y: Int, r: Int, g: Int, b: Int) {
        if (x < 0 || x >= width || y < 0 || y >= height) return
        val index = (y * width + x) * 3
        pixels[index] = r.toByte()
        pixels[index + 1] = g.toByte()
        pixels[index + 2] = b.toByte()
    }
    
    fun fillRect(x: Int, y: Int, w: Int, h: Int, r: Int, g: Int, b: Int) {
        for (py in y until (y + h).coerceAtMost(height)) {
            for (px in x until (x + w).coerceAtMost(width)) {
                setPixel(px, py, r, g, b)
            }
        }
    }
    
    fun drawRect(x: Int, y: Int, w: Int, h: Int, r: Int, g: Int, b: Int, thickness: Int = 1) {
        // Top border
        fillRect(x, y, w, thickness, r, g, b)
        // Bottom border
        fillRect(x, y + h - thickness, w, thickness, r, g, b)
        // Left border
        fillRect(x, y, thickness, h, r, g, b)
        // Right border
        fillRect(x + w - thickness, y, thickness, h, r, g, b)
    }
    
    fun drawText(x: Int, y: Int, text: String, r: Int, g: Int, b: Int) {
        // Simple 8x8 bitmap font rendering
        val charWidth = 8
        val charHeight = 12
        
        var currentX = x
        for (char in text) {
            // Draw each character using simple bitmap
            drawChar(currentX, y, char, r, g, b)
            currentX += charWidth
        }
    }
    
    private fun drawChar(x: Int, y: Int, char: Char, r: Int, g: Int, b: Int) {
        // Simple 8x12 pixel character rendering
        // This is a very basic implementation - just draws a filled rectangle for now
        // A real implementation would use bitmap fonts
        when (char) {
            ' ' -> {} // Don't draw spaces
            else -> {
                // Draw a simple representation of the character
                fillRect(x + 1, y + 2, 6, 8, r, g, b)
            }
        }
    }
    
    /**
     * Save framebuffer as PPM image format (simple, no external dependencies)
     */
    fun savePPM(filename: String) {
        val header = "P6\n$width $height\n255\n"
        memScoped {
            val file = fopen(filename, "wb")
            if (file != null) {
                fputs(header, file)
                pixels.usePinned { pinned ->
                    fwrite(pinned.addressOf(0), 1.toULong(), pixels.size.toULong(), file)
                }
                fclose(file)
                println("Saved framebuffer to $filename")
            } else {
                println("Failed to open file $filename for writing")
            }
        }
    }
}

/**
 * Graphics renderer that renders DisplayCommands to a framebuffer
 */
class GraphicsRenderer(private val framebuffer: Framebuffer) {
    
    fun render(displayList: DisplayList) {
        for (command in displayList.commands) {
            when (command) {
                is DisplayCommand.SolidColor -> renderSolidColor(command)
                is DisplayCommand.Text -> renderText(command)
                is DisplayCommand.Rectangle -> renderRectangle(command)
            }
        }
    }
    
    private fun renderSolidColor(command: DisplayCommand.SolidColor) {
        val rect = command.rect
        val color = command.color
        framebuffer.fillRect(
            rect.x.toInt(),
            rect.y.toInt(),
            rect.width.toInt(),
            rect.height.toInt(),
            color.r,
            color.g,
            color.b
        )
    }
    
    private fun renderText(command: DisplayCommand.Text) {
        val rect = command.rect
        val color = command.color
        framebuffer.drawText(
            rect.x.toInt(),
            rect.y.toInt(),
            command.text,
            color.r,
            color.g,
            color.b
        )
    }
    
    private fun renderRectangle(command: DisplayCommand.Rectangle) {
        val rect = command.rect
        val color = command.borderColor
        framebuffer.drawRect(
            rect.x.toInt(),
            rect.y.toInt(),
            rect.width.toInt(),
            rect.height.toInt(),
            color.r,
            color.g,
            color.b,
            command.borderWidth.toInt().coerceAtLeast(1)
        )
    }
}
