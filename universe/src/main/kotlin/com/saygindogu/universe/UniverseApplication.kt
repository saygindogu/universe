package com.saygindogu.universe


import JavaFxApplication
import javafx.application.Application
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
class UniverseApplication

fun main(args: Array<String>) {
    Application.launch(JavaFxApplication::class.java, *args)
}
