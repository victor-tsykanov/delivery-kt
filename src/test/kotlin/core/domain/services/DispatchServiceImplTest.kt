package com.example.delivery.core.domain.services

import com.example.delivery.core.domain.model.courier.Courier
import com.example.delivery.core.domain.model.order.Order
import com.example.delivery.core.domain.model.shared.Location
import com.example.delivery.factories.CourierFactory.freeCourierAtLocationWithSpeed
import org.junit.jupiter.api.Named.named
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DispatchServiceImplTest {
    @ParameterizedTest
    @MethodSource("dispatchTestCases")
    fun `dispatch should return the fastest courier`(case: DispatchTesCase) {
        // Arrange
        val order = Order(UUID.randomUUID(), Location(1, 1))

        // Act
        val chosenCourier = DispatchServiceImpl.dispatch(order, case.couriers)

        // Assert
        assertEquals(case.couriers[case.expectedCourierIndex], chosenCourier)
    }

    @Test
    fun `dispatch should throw exception when couriers list is empty`() {
        // Arrange
        val order = Order(UUID.randomUUID(), Location(1, 1))

        // Assert
        assertFailsWith<IllegalArgumentException> {
            // Act
            DispatchServiceImpl.dispatch(order, listOf())
        }
    }

    class DispatchTesCase(
        val name: String,
        val couriers: List<Courier>,
        val expectedCourierIndex: Int,
    )

    companion object {
        @JvmStatic
        fun dispatchTestCases() = listOf(
            DispatchTesCase(
                name = "dispatch to the closest",
                couriers = listOf(
                    freeCourierAtLocationWithSpeed(x = 7, y = 1, speed = 2),
                    freeCourierAtLocationWithSpeed(x = 5, y = 1, speed = 2),
                ),
                expectedCourierIndex = 1
            ),
            DispatchTesCase(
                name = "dispatch to the first of equally close",
                couriers = listOf(
                    freeCourierAtLocationWithSpeed(x = 7, y = 1, speed = 2),
                    freeCourierAtLocationWithSpeed(x = 1, y = 7, speed = 2),
                    freeCourierAtLocationWithSpeed(x = 1, y = 9, speed = 2),
                ),
                expectedCourierIndex = 0,
            ),
            DispatchTesCase(
                name = "dispatch to the fastest of equally close",
                couriers = listOf(
                    freeCourierAtLocationWithSpeed(x = 7, y = 1, speed = 2),
                    freeCourierAtLocationWithSpeed(x = 7, y = 1, speed = 3),
                ),
                expectedCourierIndex = 1,
            ),
        ).map {
            arguments(named(it.name, it))
        }
    }
}
