package com.example.delivery.core.domain.model.courier

import com.example.delivery.core.domain.model.shared.Location
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Transport(val name: String, val speed: Int) {
    private companion object {
        const val MIN_SPEED = 1
        const val MAX_SPEED = 3
    }

    init {
        require(speed in MIN_SPEED..MAX_SPEED) {
            "Value $speed of speed is out of range, must be between $MIN_SPEED and $MAX_SPEED"
        }
        require(name.isNotBlank()) { "Transport name must not be blank" }
    }

    fun move(from: Location, to: Location): Location {
        val xDistance = abs(to.x - from.x)
        val yDistance = abs(to.y - from.y)
        val xSteps = sign(to.x - from.x) * min(xDistance, speed)
        val remainingSteps = max(0, speed - abs(xSteps))
        val ySteps = sign(to.y - from.y) * min(yDistance, remainingSteps)

        return Location(from.x + xSteps, from.y + ySteps)
    }

    private fun sign(number: Int) = if (number > 0) 1 else -1
}
