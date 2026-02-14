package com.gimlet2.browser

import com.gimlet2.browser.rendering.*
import com.gimlet2.browser.ui.*

/**
 * Main entry point for the Kotlin-Native browser with UI rendering
 * Demonstrates the custom rendering engine with actual graphics output
 */
fun main() {
    println("=".repeat(60))
    println("Kotlin-Native Browser with Graphics UI")
    println("=".repeat(60))
    println()
    
    // Create rendering engine instance
    val engine = BrowserRenderingEngine()
    
    // Create framebuffer for rendering
    val width = 800
    val height = 600
    val framebuffer = Framebuffer(width, height)
    val renderer = GraphicsRenderer(framebuffer)
    
    // Example: Simple HTML page with browser UI
    println("Rendering HTML page with browser UI...")
    val html = """
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
    """.trimIndent()
    
    val css = """
        .browser-chrome {
            background-color: #e8e8e8;
            padding: 5px;
            margin: 0px;
        }
        .url-bar {
            background-color: #ffffff;
            color: #333333;
            padding: 8px;
            margin: 5px;
            border-color: #cccccc;
            border-width: 1px;
        }
        .content {
            background-color: #ffffff;
            padding: 20px;
            margin: 10px;
        }
        h1 {
            color: #2c3e50;
            margin: 10px;
        }
        h2 {
            color: #34495e;
            margin: 10px;
        }
        p {
            color: #555555;
            margin: 10px;
        }
        .box {
            background-color: #ecf0f1;
            padding: 15px;
            margin: 10px;
            border-color: #bdc3c7;
            border-width: 2px;
        }
    """.trimIndent()
    
    println("HTML:")
    println(html)
    println()
    println("CSS:")
    println(css)
    println()
    
    // Render HTML/CSS to display list
    val displayList = engine.render(html, css, width.toFloat(), height.toFloat())
    println("Generated ${displayList.commands.size} display commands")
    
    // Render display commands to framebuffer
    renderer.render(displayList)
    
    // Save framebuffer as image
    val outputFile = "browser_output.ppm"
    framebuffer.savePPM(outputFile)
    
    println()
    println("=".repeat(60))
    println("Browser UI rendered to: $outputFile")
    println("Convert to PNG with: convert $outputFile browser_output.png")
    println("=".repeat(60))
    
    // Also show text representation
    println()
    println("Display Commands (first 10):")
    for ((index, command) in displayList.commands.take(10).withIndex()) {
        println("  ${index + 1}. ${describeCommand(command)}")
    }
    if (displayList.commands.size > 10) {
        println("  ... and ${displayList.commands.size - 10} more commands")
    }
}

/**
 * Describe a display command in human-readable format
 */
fun describeCommand(command: DisplayCommand): String {
    return when (command) {
        is DisplayCommand.SolidColor -> {
            val rect = command.rect
            val color = command.color
            "Fill rectangle (x=${rect.x}, y=${rect.y}, w=${rect.width}, h=${rect.height}) " +
            "with color rgb(${color.r}, ${color.g}, ${color.b})"
        }
        is DisplayCommand.Text -> {
            val rect = command.rect
            "Draw text '${command.text}' at (x=${rect.x}, y=${rect.y})"
        }
        is DisplayCommand.Rectangle -> {
            val rect = command.rect
            val color = command.borderColor
            "Draw border (x=${rect.x}, y=${rect.y}, w=${rect.width}, h=${rect.height}) " +
            "width=${command.borderWidth}px, color=rgb(${color.r}, ${color.g}, ${color.b})"
        }
    }
}
