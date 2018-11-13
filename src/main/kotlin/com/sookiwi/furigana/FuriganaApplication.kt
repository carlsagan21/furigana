package com.sookiwi.furigana

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.nio.file.Files
import java.nio.file.Path

@SpringBootApplication
class FuriganaApplication {
    companion object {
        val downloadedContentDir: Path = Files.createTempDirectory("line-bot")
    }
}

fun main(args: Array<String>) {
    runApplication<FuriganaApplication>(*args)
}
