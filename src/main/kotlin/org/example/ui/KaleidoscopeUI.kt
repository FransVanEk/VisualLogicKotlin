package org.example.ui

import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Stage
import javafx.util.Duration
import org.example.service.SortingService

class KaleidoscopeUI : Application() {

    companion object {
        private var sortingService: SortingService? = null
        private var numbers: List<Int>? = null

        fun launchUI(service: SortingService, nums: List<Int>) {
            sortingService = service
            numbers = nums
            launch(KaleidoscopeUI::class.java)
        }
    }

    private val quadrants: MutableList<List<Rectangle>> = mutableListOf()
    private val windowHeight = 800.0
    private val windowWidth = 800.0
    private val delayMs = 30

    override fun start(primaryStage: Stage) {
        val sortingService = sortingService
        val numbers = numbers
        if (sortingService == null || numbers == null) {
            throw IllegalStateException("KaleidoscopeUI is not properly configured. Make sure to call launchUI().")
        }

        // Calculate scale factors
        val maxValue = numbers.maxOrNull() ?: 1
        val scaleFactorHeight = (windowHeight / 2 - 50) / maxValue
        val barWidth = (windowWidth / 2 - 50) / numbers.size

        // Main container
        val root = Pane().apply {
            style = "-fx-background-color: black;"
        }

        // Create the four quadrants
        quadrants.add(createBarChartPane(barWidth, scaleFactorHeight, 0.0))
        quadrants.add(createBarChartPane(barWidth, scaleFactorHeight, 90.0))
        quadrants.add(createBarChartPane(barWidth, scaleFactorHeight, 180.0))
        quadrants.add(createBarChartPane(barWidth, scaleFactorHeight, 270.0))

        quadrants.forEachIndexed { index, bars ->
            val pane = Pane().apply {
                bars.forEach { children.add(it) }
                rotate = index * 90.0
                translateX = if (index == 1 || index == 3) windowWidth / 2 else 0.0
                translateY = if (index >= 2) windowHeight / 2 else 0.0
            }
            root.children.add(pane)
        }

        // Animation based on sorting steps
        val steps = sortingService.sort(numbers.toMutableList())
        val timeline = createSortingAnimation(steps)

        // Set up the stage
        primaryStage.scene = Scene(root, windowWidth, windowHeight)
        primaryStage.title = "Kaleidoscope Sort"
        primaryStage.show()

        // Start the animation
        timeline.play()
    }

    private fun createBarChartPane(barWidth: Double, scaleFactorHeight: Double, rotationAngle: Double): List<Rectangle> {
        return numbers!!.mapIndexed { index, number ->
            val barHeight = number * scaleFactorHeight
            Rectangle(barWidth, barHeight).apply {
                fill = Color.hsb(360.0 * index / numbers!!.size, 0.8, 0.8)
                x = barWidth * index
                y = (windowHeight / 2) - barHeight
            }
        }
    }

    private fun createSortingAnimation(steps: List<List<Int>>): Timeline {
        val timeline = Timeline()
        timeline.cycleCount = 1
        timeline.isAutoReverse = false

        steps.forEachIndexed { index, step ->
            val duration = Duration.millis((delayMs * index).toDouble())
            val keyFrame = KeyFrame(duration, {
                updateBars(step)
            })
            timeline.keyFrames.add(keyFrame)
        }

        return timeline
    }


    private fun updateBars(step: List<Int>) {
        val scaleFactorHeight = (windowHeight / 2 - 50) / (step.maxOrNull() ?: 1)
        val barWidth = (windowWidth / 2 - 50) / step.size

        quadrants.forEach { bars ->
            step.forEachIndexed { index, value ->
                val barHeight = value * scaleFactorHeight
                bars[index].apply {
                    height = barHeight
                    width = barWidth
                    y = (windowHeight / 2) - barHeight
                    fill = Color.hsb(360.0 * index / step.size, 0.8, 0.8)
                }
            }
        }
    }
}
