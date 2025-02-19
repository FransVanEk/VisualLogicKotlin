package org.example.service

import org.example.model.SortAlgorithm

class SortingService(private val sortAlgorithm: SortAlgorithm) {
    fun sort(numbers: List<Int>): List<List<Int>> = sortAlgorithm.sort(numbers)
}
