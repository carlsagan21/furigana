package com.sookiwi.furigana

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.nio.file.Files
import java.nio.file.Path

@SpringBootApplication
class FuriganaApplication {

//    @Bean
//    fun webClientCustomizer(): WebClientCustomizer {
//        return WebClientCustomizer { webClientBuilder ->
//            webClientBuilder.baseUrl("")
//        }
//    }
}

fun main(args: Array<String>) {
    runApplication<FuriganaApplication>(*args)
}

val downloadedContentDir: Path = Files.createTempDirectory("line-bot")
