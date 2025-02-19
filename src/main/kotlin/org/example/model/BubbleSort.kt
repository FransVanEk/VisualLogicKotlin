package org.example.model

class BubbleSort : SortAlgorithm {
    override fun sort(numbers: List<Int>): List<List<Int>> {
        val steps = mutableListOf(numbers.toMutableList())
        val nums = numbers.toMutableList()
        for (i in nums.indices) {
            for (j in 0 until nums.size - i - 1) {
                if (nums[j] > nums[j + 1]) {
                    val temp = nums[j]
                    nums[j] = nums[j + 1]
                    nums[j + 1] = temp
                }
            }
            steps.add(nums.toMutableList())
        }
        return steps
    }
}
