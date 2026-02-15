package com.gimlet2.browser

import com.gimlet2.browser.rendering.*
import com.gimlet2.browser.ui.*

/**
 * Main entry point for the Kotlin-Native browser with window UI
 * Demonstrates the custom rendering engine with window display
 */
fun main() {
    println("=".repeat(60))
    println("Kotlin-Native Browser with Window UI")
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
                <p>SDL2 window display</p>
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
    
    // Render HTML/CSS to display list
    val displayList = engine.render(html, css, width.toFloat(), height.toFloat())
    println("Generated ${displayList.commands.size} display commands")
    
    // Render display commands to framebuffer
    renderer.render(displayList)
    
    println()
    println("=".repeat(60))
    println("Opening browser window...")
    println("Press ESC or Q to close, or close the window.")
    println("=".repeat(60))
    println()
    
    // Create and run window
    val window = BrowserWindow(width, height, "Kotlin-Native Browser - https://example.com")
    window.run(framebuffer)
    
    println()
    println("Browser closed.")
}
