package com.saygindogu.universe

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.text.Text
import org.springframework.stereotype.Component
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Component
class SimpleUIController {

    @FXML
    lateinit var label: Label

    @FXML
    lateinit var simulateButton: Button

    @FXML
    lateinit var showButton: Button

    @FXML
    lateinit var abortButton: Button


    @FXML
    lateinit var fileName: TextField

    @FXML
    lateinit var iterations: TextField

    @FXML
    lateinit var benchmark: CheckBox

    @FXML
    lateinit var progressbar: ProgressBar

    @FXML
    lateinit var interactionIndicator: Text

    @FXML
    fun initialize() {
        fileName.text = "history1.txt"
        iterations.text = "1001"
        benchmark.isSelected = false

        lateinit var executor: ExecutorService
        abortButton.setOnAction {
            executor.shutdownNow()
            simulateButton.isDisable = false
            val f = File(fileName.text)
            showButton.isDisable = !f.exists()

        }
        simulateButton.setOnAction {
            simulateButton.isDisable = true
            showButton.isDisable = true
            label.text = "simulating..."
            executor = Executors.newFixedThreadPool(1)
            executor.submit {
                val sim = SimulationInitializer().initializeSim()
                sim.run(
                    iterations.text.toInt(),
                    fileName.text,
                    benchmark.isSelected
                ) { stats ->
                    Platform.runLater {
                        progressbar.progress = stats.progress
                        interactionIndicator.text = stats.summary()
                    }
                }
                Platform.runLater {
                    label.text = "done"
                    simulateButton.isDisable = false
                    showButton.isDisable = false
                }
                executor.shutdown()
                executor.awaitTermination(3, TimeUnit.SECONDS)
            }
        }
        showButton.setOnAction {
            SimulationView().showView(fileName.text)
        }

        fileName.setOnKeyReleased {
            val f = File(fileName.text)
            showButton.isDisable = !f.exists()
        }
    }
}