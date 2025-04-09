package com.example.delivery.core.domain.model.courier

import com.example.delivery.core.domain.model.courier.Courier
import com.example.delivery.core.domain.model.shared.Location
import org.junit.jupiter.api.Named.named
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class CourierTest {
    @Test
    fun `setBusy should change courier status to busy when courier is free`() {
        // Arrange
        val courier = Courier("Bob", "car", 3, Location(4, 5))

        // Act
        courier.setBusy()

        // Assert
        assertEquals(Courier.Status.BUSY, courier.status)
    }

    @Test
    fun `setBusy should throw exception when courier is busy`() {
        // Arrange
        val courier = Courier("Bob", "car", 3, Location(4, 5))
        courier.setBusy()

        // Assert
        assertFailsWith<IllegalStateException> {
            // Act
            courier.setBusy()
        }
    }

    @Test
    fun `setFree should change courier status to free when courier is busy`() {
        // Arrange
        val courier = Courier("Bob", "car", 3, Location(4, 5))
        courier.setBusy()

        // Act
        courier.setFree()

        // Assert
        assertEquals(Courier.Status.FREE, courier.status)
    }

    @Test
    fun `setFree should throw exception when courier is free`() {
        // Arrange
        val courier = Courier("Bob", "car", 3, Location(4, 5))

        // Assert
        assertFailsWith<IllegalStateException> {
            // Act
            courier.setFree()
        }
    }

    @Test
    fun `move should change courier location`() {
        // Arrange
        val currentLocation = Location(1, 1)
        val targetLocation = Location(1, 9)
        val expectedLocation = Location(1, 4)
        val courier = Courier("Bob", "car", 3, currentLocation)

        // Act
        courier.move(targetLocation)

        // Assert
        assertEquals(expectedLocation, courier.location)
    }

    @ParameterizedTest
    @MethodSource("calculateStepsToLocationTestCases")
    fun `calculateStepsToLocation return number of steps to target location`(case: CalculateStepsToLocationTesCase) {
        // Arrange
        val courier = Courier("Bob", "car", 3, case.from)

        // Act
        val steps = courier.calculateStepsToLocation(case.to)

        // Assert
        assertEquals(case.expectedSteps, steps)
    }

    class CalculateStepsToLocationTesCase(
        val name: String,
        val from: Location,
        val to: Location,
        val expectedSteps: Int,
    )

    companion object {
        @JvmStatic
        fun calculateStepsToLocationTestCases() = listOf(
            CalculateStepsToLocationTesCase(
                name = "same location",
                from = Location(1, 1),
                to = Location(1, 1),
                expectedSteps = 0,
            ),
            CalculateStepsToLocationTesCase(
                name = "with no remaining distance",
                from = Location(1, 1),
                to = Location(7, 1),
                expectedSteps = 2,
            ),
            CalculateStepsToLocationTesCase(
                name = "with remaining distance",
                from = Location(1, 1),
                to = Location(9, 1),
                expectedSteps = 3,
            ),
        ).map {
            arguments(named(it.name, it))
        }
    }
}
