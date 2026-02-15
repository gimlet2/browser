package com.gimlet2.browser.rendering

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class HtmlParserTest {
    
    private val parser = HtmlParser()
    
    @Test
    fun `parse simple HTML element`() {
        val html = "<div>Hello World</div>"
        val document = parser.parse(html)
        
        assertEquals(1, document.children.size)
        val element = document.children[0] as ElementNode
        assertEquals("div", element.tagName)
        assertEquals(1, element.children.size)
        
        val text = element.children[0] as TextNode
        assertEquals("Hello World", text.text)
    }
    
    @Test
    fun `parse HTML with attributes`() {
        val html = """<div id="main" class="container">Content</div>"""
        val document = parser.parse(html)
        
        val element = document.children[0] as ElementNode
        assertEquals("main", element.getAttribute("id"))
        assertEquals("container", element.getAttribute("class"))
    }
    
    @Test
    fun `parse nested HTML elements`() {
        val html = """
            <html>
                <body>
                    <h1>Title</h1>
                    <p>Paragraph</p>
                </body>
            </html>
        """.trimIndent()
        
        val document = parser.parse(html)
        assertNotNull(document)
        
        val html2 = document.children[0] as ElementNode
        assertEquals("html", html2.tagName)
        
        val body = html2.children[0] as ElementNode
        assertEquals("body", body.tagName)
        assertEquals(2, body.children.size)
        
        val h1 = body.children[0] as ElementNode
        assertEquals("h1", h1.tagName)
        
        val p = body.children[1] as ElementNode
        assertEquals("p", p.tagName)
    }
    
    @Test
    fun `parse self-closing tags`() {
        val html = """<img src="test.jpg" />"""
        val document = parser.parse(html)
        
        val element = document.children[0] as ElementNode
        assertEquals("img", element.tagName)
        assertEquals("test.jpg", element.getAttribute("src"))
        assertEquals(0, element.children.size)
    }
    
    @Test
    fun `getElementById returns correct element`() {
        val html = """<div id="test">Content</div>"""
        val document = parser.parse(html)
        
        val element = document.getElementById("test")
        assertNotNull(element)
        assertEquals("div", element.tagName)
    }
}
