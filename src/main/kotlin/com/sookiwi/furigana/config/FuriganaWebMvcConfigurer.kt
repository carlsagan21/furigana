package com.sookiwi.furigana.config

import com.sookiwi.furigana.FuriganaApplication
import mu.KLogger
import mu.KotlinLogging
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class FuriganaWebMvcConfigurer : WebMvcConfigurer {
    private val log: KLogger = KotlinLogging.logger {}

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val downloadedContentUri = FuriganaApplication.downloadedContentDir.toUri().toASCIIString()

        log.info { "downloaded dir: $downloadedContentUri" }
        registry.addResourceHandler("/downloaded/**")
            .addResourceLocations(downloadedContentUri)
        registry.addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/")
    }
}
