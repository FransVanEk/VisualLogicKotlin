package org.example.ui

interface DisplayUI {
    fun launchUI(sortingService: org.example.service.SortingService, numbers: List<Int>)
}
