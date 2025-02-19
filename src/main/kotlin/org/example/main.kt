package org.example

import javafx.application.Application
import org.example.service.SortingService
import org.example.model.BubbleSort
import org.example.ui.KaleidoscopeUI
import org.example.ui.VisualizationUI
import org.example.utils.NumberGenerator

fun main() {
    // Configure SortingService with BubbleSort algorithm
    val sortingService = SortingService(BubbleSort())

    // Generate a list of random numbers
    val numbers = NumberGenerator().generate(120, 1, 50)

    // Launch the Visualization UI
    KaleidoscopeUI.launchUI(sortingService, numbers)
}

