package com.example.delivery.core.domain.model.order

import com.example.delivery.core.domain.model.courier.Courier
import com.example.delivery.core.domain.model.order.Order
import com.example.delivery.core.domain.model.shared.Location
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class OrderTest {
    @Test
    fun `assign should assign order to courier when order is in created status`() {
        // Arrange
        val order = Order(UUID.randomUUID(), Location(1, 1))
        val courier = Courier("Bob", "car", 3, Location(4, 5))

        // Act
        order.assign(courier)

        // Assert
        assertEquals(Order.Status.ASSIGNED, order.status)
        assertEquals(courier.id, order.courierId)
    }

    @Test
    fun `assign should throw exception when order is already assigned`() {
        // Arrange
        val order = Order(UUID.randomUUID(), Location(1, 1))
        val courier = Courier("Bob", "car", 3, Location(4, 5))
        order.assign(courier)

        // Assert
        assertFailsWith<IllegalStateException> {
            // Act
            order.assign(courier)
        }
    }


    @Test
    fun `assign should throw exception when order is completed`() {
        // Arrange
        val order = Order(UUID.randomUUID(), Location(1, 1))
        val courier = Courier("Bob", "car", 3, Location(4, 5))
        order.assign(courier)
        order.complete()

        // Assert
        assertFailsWith<IllegalStateException> {
            // Act
            order.assign(courier)
        }
    }

    @Test
    fun `complete should put order into completed status when order is assigned`() {
        // Arrange
        val order = Order(UUID.randomUUID(), Location(1, 1))
        val courier = Courier("Bob", "car", 3, Location(4, 5))
        order.assign(courier)

        // Act
        order.complete()

        // Assert
        assertEquals(Order.Status.COMPLETED, order.status)
    }

    @Test
    fun `complete should throw exception when order is not assigned`() {
        // Arrange
        val order = Order(UUID.randomUUID(), Location(1, 1))

        // Assert
        assertFailsWith<IllegalStateException> {
            // Act
            order.complete()
        }
    }

    @Test
    fun `complete should throw exception when order is already completed`() {
        // Arrange
        val order = Order(UUID.randomUUID(), Location(1, 1))

        // Assert
        assertFailsWith<IllegalStateException> {
            // Act
            order.complete()
        }
    }
}
