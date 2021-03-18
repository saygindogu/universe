package com.saygindogu.universe

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component

@Component
class StateListener(
    @Value("\${spring.application.ui.title}") val applicationTitle: String,
    @Value("classpath:/ui.fxml") val resource: Resource,
    @Value("classpath:/history.txt") val history: Resource,
    val applicationContext: ApplicationContext
) : ApplicationListener<StageReady> {

    lateinit var primaryStage: Stage

    override fun onApplicationEvent(event: StageReady) {
        primaryStage = event.stage
        val fxmlLoader = FXMLLoader(resource.url)
        fxmlLoader.setControllerFactory({ x -> applicationContext.getBean(x) })
        val root: Parent = fxmlLoader.load()
        val scene = Scene(root, 1920.0, 1080.0)
        primaryStage.scene = scene
        primaryStage.title = applicationTitle
        primaryStage.show()
    }
}