package com.sookiwi.furigana

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FuriganaApplication

fun main(args: Array<String>) {
    runApplication<FuriganaApplication>(*args)
}
