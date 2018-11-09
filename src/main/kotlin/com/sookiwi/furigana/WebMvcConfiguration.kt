package com.sookiwi.furigana

import mu.KotlinLogging
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebMvcConfiguration : WebMvcConfigurer {
    private val log = KotlinLogging.logger {}

    override fun addFormatters(registry: FormatterRegistry) {
        log.info { "WebMvcConfiguration.addFormatters" }
        log.info { registry }
        registry.addConverter(TextMessageEventConverter())
    }
}
