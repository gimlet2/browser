package com.gimlet2.browser.rendering

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class RenderingEngineTest {
    
    @Test
    fun `render simple HTML`() {
        val engine = BrowserRenderingEngine()
        val html = "<div>Hello World</div>"
        
        val displayList = engine.renderHtml(html, 800f, 600f)
        
        assertNotNull(displayList)
        assertTrue(displayList.commands.isNotEmpty())
    }
    
    @Test
    fun `render HTML with CSS`() {
        val engine = BrowserRenderingEngine()
        val html = """<div class="box">Styled content</div>"""
        val css = """
            .box {
                background-color: #ff0000;
                width: 200px;
                padding: 10px;
            }
        """.trimIndent()
        
        val displayList = engine.render(html, css, 800f, 600f)
        
        assertNotNull(displayList)
        assertTrue(displayList.commands.isNotEmpty())
        
        // Check that we have background color command
        val hasBackgroundColor = displayList.commands.any { it is DisplayCommand.SolidColor }
        assertTrue(hasBackgroundColor)
    }
    
    @Test
    fun `layout calculates dimensions`() {
        val htmlParser = HtmlParser()
        val cssParser = CssParser()
        val styleTreeBuilder = StyleTreeBuilder()
        val layoutEngine = LayoutEngine()
        
        val html = """<div>Content</div>"""
        val css = """div { width: 100px; height: 50px; }"""
        
        val document = htmlParser.parse(html)
        val stylesheet = cssParser.parse(css)
        val styledNode = styleTreeBuilder.styleTree(document, stylesheet)!!
        
        val viewport = Dimensions(content = Rect(0f, 0f, 800f, 600f))
        val layoutBox = layoutEngine.layout(styledNode, viewport)
        
        assertNotNull(layoutBox)
        // Check that layout has been computed
        assertTrue(layoutBox.dimensions.content.width > 0)
    }
    
    @Test
    fun `style tree builder matches selectors`() {
        val htmlParser = HtmlParser()
        val cssParser = CssParser()
        val styleTreeBuilder = StyleTreeBuilder()
        
        val html = """<div class="container" id="main">Content</div>"""
        val css = """
            #main { width: 200px; }
            .container { padding: 10px; }
        """.trimIndent()
        
        val document = htmlParser.parse(html)
        val stylesheet = cssParser.parse(css)
        val styledNode = styleTreeBuilder.styleTree(document, stylesheet)
        
        assertNotNull(styledNode)
        assertTrue(styledNode.children.isNotEmpty())
        
        val divNode = styledNode.children[0]
        assertTrue(divNode.styles.isNotEmpty())
    }
    
    @Test
    fun `display list can be rendered to string`() {
        val engine = BrowserRenderingEngine()
        val renderer = SimpleRenderer()
        
        val html = """<div>Test</div>"""
        val css = """div { background-color: #0000ff; }"""
        
        val displayList = engine.render(html, css, 800f, 600f)
        val output = renderer.renderToString(displayList)
        
        assertNotNull(output)
        assertTrue(output.contains("Display List"))
    }
}
