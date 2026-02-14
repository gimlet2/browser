package com.gimlet2.browser.rendering

/**
 * Main rendering engine facade that coordinates parsing, styling, layout, and painting
 */
class BrowserRenderingEngine {
    
    private val htmlParser = HtmlParser()
    private val cssParser = CssParser()
    private val styleTreeBuilder = StyleTreeBuilder()
    private val layoutEngine = LayoutEngine()
    private val renderingEngine = RenderingEngine()
    
    /**
     * Render HTML and CSS to display list
     */
    fun render(html: String, css: String, viewportWidth: Float, viewportHeight: Float): DisplayList {
        // 1. Parse HTML into DOM tree
        val document = htmlParser.parse(html)
        
        // 2. Parse CSS into stylesheet
        val stylesheet = cssParser.parse(css)
        
        // 3. Build styled tree by matching CSS to DOM
        val styledNode = styleTreeBuilder.styleTree(document, stylesheet)
            ?: return DisplayList(emptyList())
        
        // 4. Perform layout to compute positions and sizes
        val viewport = Dimensions(
            content = Rect(0f, 0f, viewportWidth, viewportHeight)
        )
        val layoutBox = layoutEngine.layout(styledNode, viewport)
        
        // 5. Paint layout boxes to display commands
        return renderingEngine.paint(layoutBox)
    }
    
    /**
     * Render HTML only (without CSS)
     */
    fun renderHtml(html: String, viewportWidth: Float, viewportHeight: Float): DisplayList {
        return render(html, "", viewportWidth, viewportHeight)
    }
}
