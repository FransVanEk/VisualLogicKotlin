package org.example.ui

import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Stage
import javafx.util.Duration
import org.example.service.SortingService

class VisualizationUI : Application() {

    companion object {
        private var sortingService: SortingService? = null
        private var numbers: List<Int>? = null

        fun configure(service: SortingService, nums: List<Int>) {
            sortingService = service
            numbers = nums
        }

        fun launchUI(service: SortingService, nums: List<Int>) {
            sortingService = service
            numbers = nums
            launch(VisualizationUI::class.java) // Start de daadwerkelijke klasse
        }
    }

    private val bars = mutableListOf<Rectangle>()
    private val timeSec = 10
    private val windowHeight = 600.0
    private val windowWidth = 1400.0
    private val spacing = 0.0

    override fun start(primaryStage: Stage) {
        val sortingService = sortingService
        val numbers = numbers
        if (sortingService == null || numbers == null) {
            throw IllegalStateException("VisualizationUI is not properly configured. Make sure to call configure().")
        }

        // Calculate scale factors
        val maxValue = numbers.maxOrNull() ?: 1
        val scaleFactorHeight = (windowHeight - 100) / maxValue
        val scaleFactorWidth = (windowWidth - 100) / numbers.size.toDouble().coerceAtLeast(2.0)

        // Main container
        val root = VBox().apply {
            style = "-fx-padding: 20; -fx-background-color: #f0f0f0;"
        }

        // Bar container
        val barContainer = HBox(spacing).apply {
            style = "-fx-alignment: bottom-center;"
            prefHeight = windowHeight - 50
        }

        // Create visual bars
        numbers.forEach { number ->
            val barHeight = number * scaleFactorHeight
            val barWidth = scaleFactorWidth
            val bar = Rectangle(barWidth, barHeight).apply {
                fill = Color.BLUE
            }
            bars.add(bar)
            barContainer.children.add(bar)
        }

        // Make scrollable if there are too many bars
        val scrollPane = ScrollPane(barContainer).apply {
            isFitToHeight = true
            isPannable = true
        }

        root.children.add(scrollPane)

        // Create timeline for animation
        val steps = sortingService.sort(ArrayList(numbers))
        val timeline = createSortAnimation(steps, scaleFactorHeight)

        // Set up the stage
        primaryStage.scene = Scene(root, windowWidth, windowHeight)
        primaryStage.title = "Sorting Visualization"
        primaryStage.show()

        // Start animation
        timeline.play()
    }

    private fun createSortAnimation(steps: List<List<Int>>, scaleFactorHeight: Double): Timeline {
        val timeline = Timeline()
        timeline.cycleCount = 1
        timeline.isAutoReverse = false

        steps.forEachIndexed { index, step ->
            val duration = Duration.millis((timeSec * 1000 / steps.size) * index.toDouble())
            val keyFrame = KeyFrame(duration, {
                updateBars(step, scaleFactorHeight)
            })
            timeline.keyFrames.add(keyFrame)
        }

        return timeline
    }

    private fun updateBars(step: List<Int>, scaleFactorHeight: Double) {
        step.forEachIndexed { index, value ->
            bars[index].height = value * scaleFactorHeight
        }
    }
}
