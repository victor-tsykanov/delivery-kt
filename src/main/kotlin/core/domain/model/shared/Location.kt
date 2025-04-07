package com.example.delivery.core.domain.model.shared


data class Location(val x: Int, val y: Int) {
    private companion object {
        const val MIN_X = 1
        const val MAX_X = 10
        const val MIN_Y = 1
        const val MAX_Y = 10
    }

    init {
        require(x in MIN_X..MAX_X) { "Value $x of X is out of range, must be between $MIN_X and $MAX_X" }
        require(y in MIN_Y..MAX_Y) { "Value $y of Y is out of range, must be between $MIN_Y and $MAX_Y" }
    }
}
