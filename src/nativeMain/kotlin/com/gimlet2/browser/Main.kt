package com.gimlet2.browser

import com.gimlet2.browser.rendering.*

/**
 * Main entry point for the Kotlin-Native browser rendering engine
 * Demonstrates the custom rendering engine built from scratch
 */
fun main() {
    println("=".repeat(60))
    println("Kotlin-Native Browser Rendering Engine")
    println("=".repeat(60))
    println()
    
    // Create rendering engine instance
    val engine = BrowserRenderingEngine()
    
    // Example 1: Simple HTML
    println("Example 1: Simple HTML")
    println("-".repeat(60))
    val html1 = """
        <div>
            <h1>Hello from Kotlin-Native!</h1>
            <p>This is rendered by the custom engine.</p>
        </div>
    """.trimIndent()
    
    val css1 = """
        div {
            background-color: #f0f0f0;
            padding: 20px;
        }
        h1 {
            color: #2c3e50;
        }
        p {
            color: #34495e;
        }
    """.trimIndent()
    
    println("HTML:")
    println(html1)
    println()
    println("CSS:")
    println(css1)
    println()
    
    val displayList1 = engine.render(html1, css1, 800f, 600f)
    println("Generated ${displayList1.commands.size} display commands:")
    for ((index, command) in displayList1.commands.withIndex()) {
        println("  ${index + 1}. ${describeCommand(command)}")
    }
    println()
    
    // Example 2: Styled Box
    println("Example 2: Styled Box with Border")
    println("-".repeat(60))
    val html2 = """
        <div class="box">
            <h2>Styled Container</h2>
            <p>This box has background, padding, and borders.</p>
        </div>
    """.trimIndent()
    
    val css2 = """
        .box {
            background-color: #3498db;
            color: #ffffff;
            padding: 15px;
            margin: 10px;
            border-color: #2980b9;
            border-width: 2px;
            width: 400px;
        }
        h2 {
            margin: 10px;
        }
        p {
            margin: 10px;
        }
    """.trimIndent()
    
    println("HTML:")
    println(html2)
    println()
    println("CSS:")
    println(css2)
    println()
    
    val displayList2 = engine.render(html2, css2, 800f, 600f)
    println("Generated ${displayList2.commands.size} display commands:")
    for ((index, command) in displayList2.commands.withIndex()) {
        println("  ${index + 1}. ${describeCommand(command)}")
    }
    println()
    
    // Example 3: Multiple Elements
    println("Example 3: Multiple Elements")
    println("-".repeat(60))
    val html3 = """
        <div id="container">
            <div class="item">Item 1</div>
            <div class="item">Item 2</div>
            <div class="item">Item 3</div>
        </div>
    """.trimIndent()
    
    val css3 = """
        #container {
            background-color: #ecf0f1;
            padding: 10px;
        }
        .item {
            background-color: #e74c3c;
            color: #ffffff;
            padding: 10px;
            margin: 5px;
        }
    """.trimIndent()
    
    println("HTML:")
    println(html3)
    println()
    println("CSS:")
    println(css3)
    println()
    
    val displayList3 = engine.render(html3, css3, 800f, 600f)
    println("Generated ${displayList3.commands.size} display commands:")
    for ((index, command) in displayList3.commands.withIndex()) {
        println("  ${index + 1}. ${describeCommand(command)}")
    }
    println()
    
    println("=".repeat(60))
    println("Rendering engine demonstration complete!")
    println("=".repeat(60))
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
