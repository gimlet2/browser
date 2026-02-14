package com.gimlet2.browser.rendering

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CssParserTest {
    
    private val parser = CssParser()
    
    @Test
    fun `parse simple CSS rule`() {
        val css = """
            div {
                color: #ff0000;
                width: 100px;
            }
        """.trimIndent()
        
        val stylesheet = parser.parse(css)
        
        assertEquals(1, stylesheet.rules.size)
        val rule = stylesheet.rules[0]
        
        assertEquals(1, rule.selectors.size)
        val selector = rule.selectors[0] as Selector.Simple
        assertEquals("div", selector.tagName)
        
        assertEquals(2, rule.declarations.size)
    }
    
    @Test
    fun `parse CSS with class selector`() {
        val css = """.container { margin: 10px; }"""
        val stylesheet = parser.parse(css)
        
        val selector = stylesheet.rules[0].selectors[0] as Selector.Simple
        assertTrue(selector.classes.contains("container"))
    }
    
    @Test
    fun `parse CSS with id selector`() {
        val css = """#main { padding: 20px; }"""
        val stylesheet = parser.parse(css)
        
        val selector = stylesheet.rules[0].selectors[0] as Selector.Simple
        assertEquals("main", selector.id)
    }
    
    @Test
    fun `parse CSS color values`() {
        val css = """div { color: #ff0000; }"""
        val stylesheet = parser.parse(css)
        
        val declaration = stylesheet.rules[0].declarations[0]
        val color = declaration.value as CssValue.Color
        assertEquals(255, color.r)
        assertEquals(0, color.g)
        assertEquals(0, color.b)
    }
    
    @Test
    fun `parse CSS length values`() {
        val css = """div { width: 100px; margin: 2em; }"""
        val stylesheet = parser.parse(css)
        
        val widthDecl = stylesheet.rules[0].declarations[0]
        val width = widthDecl.value as CssValue.Length
        assertEquals(100f, width.value)
        assertEquals(CssUnit.PX, width.unit)
        
        val marginDecl = stylesheet.rules[0].declarations[1]
        val margin = marginDecl.value as CssValue.Length
        assertEquals(2f, margin.value)
        assertEquals(CssUnit.EM, margin.unit)
    }
    
    @Test
    fun `parse multiple CSS rules`() {
        val css = """
            h1 { font-size: 24px; }
            p { margin: 10px; }
            .highlight { background-color: #ffff00; }
        """.trimIndent()
        
        val stylesheet = parser.parse(css)
        assertEquals(3, stylesheet.rules.size)
    }
}
