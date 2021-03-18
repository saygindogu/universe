package com.saygindogu.universe

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.chart.BarChart
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.stage.Stage
import javafx.stage.WindowEvent
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class SimulationView {

    fun showView(fileName: String) {
        val file = BufferedReader(FileReader(File(fileName)))
        val xAxis = NumberAxis()
        val yAxis = CategoryAxis()
        xAxis.label = "Fundemental Value"
        xAxis.animated = false
        yAxis.label = "Name"
        yAxis.animated = false
        val lineChart = BarChart(xAxis, yAxis)
        lineChart.title = "Universe"
        lineChart.animated = false

        val series = XYChart.Series<Number, String>()
        series.name = "Data Series"
        lineChart.data.add(series)

        val executor = Executors.newSingleThreadScheduledExecutor()
        val stage = Stage()
        executor.scheduleAtFixedRate({
            Platform.runLater {
                try {
                    val newSeries = XYChart.Series<Number, String>()
                    newSeries.name = "Data Series"
                    newSeries.data.addAll(nextSpaceInstance(file).map {
                        XYChart.Data<Number, String>(
                            it.first,
                            it.second
                        )
                    })
                    lineChart.data.clear()
                    lineChart.data.add(newSeries)
                } catch (e: Exception) {
                    e.printStackTrace()
                    executor.shutdown()
                    executor.awaitTermination(3, TimeUnit.SECONDS)
                    stage.close()
                }

            }
        }, 0, 100, TimeUnit.MILLISECONDS)
        stage.scene = Scene(lineChart, 1920.0, 1080.0)
        stage.onCloseRequest = EventHandler<WindowEvent> { executor.shutdownNow() }
        stage.show()
    }

    private fun nextSpaceInstance(lineIterator: BufferedReader): MutableList<Pair<Double, String>> {
        val list = mutableListOf<Pair<Double, String>>()
        var line = lineIterator.readLine()
        do {
            if (line != null && line.startsWith("name:")) {
                val tokens = line.split("\t")
                var name = tokens[0].split("name:")[1]
                if( name.length > 10){
                    name = "${name.substring(0, 10)}..."
                }
                val fundementalValue = tokens[1].split("value: ")[1].toDouble()
                list.add(Pair(fundementalValue, name))
            }
            line = lineIterator.readLine()
        } while (line != null && !line.startsWith("="))
        return list
    }
}