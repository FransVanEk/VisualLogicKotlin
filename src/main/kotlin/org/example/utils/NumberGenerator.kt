package org.example.utils

import kotlin.random.Random

class NumberGenerator {
    fun generate(count: Int, min: Int, max: Int): List<Int> {
        require(count > 0) { "Count must be greater than 0" }
        require(min <= max) { "Min must be less than or equal to Max" }

        return List(count) { Random.nextInt(min, max + 1) }
    }
}
