package com.sookiwi.furigana.config

import com.sookiwi.furigana.downloadedContentDir
import mu.KotlinLogging
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

private val logger = KotlinLogging.logger {}

@Configuration
class FuriganaWebMvcConfigurer : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val downloadedContentUri = downloadedContentDir.toUri().toASCIIString()

        logger.info { "downloaded dir: $downloadedContentUri" }
        registry.addResourceHandler("/downloaded/**")
            .addResourceLocations(downloadedContentUri)
        registry.addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/")
    }
}
