package com.gimlet2.browser

import okhttp3.OkHttpClient
import okhttp3.Protocol
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

/**
 * Tests for HTTP protocol support in the browser.
 */
class HttpProtocolTest {
    
    @Test
    fun `test OkHttp client supports HTTP 1_1`() {
        val client = OkHttpClient.Builder().build()
        val supportedProtocols = client.protocols
        assertTrue(supportedProtocols.contains(Protocol.HTTP_1_1), 
            "Client should support HTTP/1.1")
    }
    
    @Test
    fun `test OkHttp client supports HTTP 2`() {
        val client = OkHttpClient.Builder().build()
        val supportedProtocols = client.protocols
        assertTrue(supportedProtocols.contains(Protocol.HTTP_2), 
            "Client should support HTTP/2")
    }
    
    @Test
    fun `test OkHttp client has default protocols`() {
        val client = OkHttpClient.Builder().build()
        val supportedProtocols = client.protocols
        
        // OkHttp supports HTTP/2, HTTP/1.1 by default
        assertTrue(supportedProtocols.isNotEmpty(), 
            "Client should have protocols configured")
        assertTrue(supportedProtocols.size >= 2, 
            "Client should support at least 2 protocols")
    }
}
