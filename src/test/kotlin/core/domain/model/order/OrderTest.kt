package com.example.delivery.core.domain.model.order

import com.example.delivery.factories.CourierFactory.freeCourier
import com.example.delivery.factories.OrderFactory.assignedOrder
import com.example.delivery.factories.OrderFactory.completedOrder
import com.example.delivery.factories.OrderFactory.unassignedOrder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class OrderTest {
    @Test
    fun `assign should assign order to courier when order is in created status`() {
        // Arrange
        val order = unassignedOrder()
        val courier = freeCourier()

        // Act
        order.assign(courier)

        // Assert
        assertEquals(Order.Status.ASSIGNED, order.status)
        assertEquals(courier.id, order.courierId)
    }

    @Test
    fun `assign should throw exception when order is already assigned`() {
        // Arrange
        val order = assignedOrder()
        val courier = freeCourier()

        // Assert
        assertFailsWith<IllegalStateException> {
            // Act
            order.assign(courier)
        }
    }


    @Test
    fun `assign should throw exception when order is completed`() {
        // Arrange
        val order = completedOrder()
        val courier = freeCourier()

        // Assert
        assertFailsWith<IllegalStateException> {
            // Act
            order.assign(courier)
        }
    }

    @Test
    fun `complete should put order into completed status when order is assigned`() {
        // Arrange
        val order = assignedOrder()

        // Act
        order.complete()

        // Assert
        assertEquals(Order.Status.COMPLETED, order.status)
    }

    @Test
    fun `complete should throw exception when order is not assigned`() {
        // Arrange
        val order = unassignedOrder()

        // Assert
        assertFailsWith<IllegalStateException> {
            // Act
            order.complete()
        }
    }

    @Test
    fun `complete should throw exception when order is already completed`() {
        // Arrange
        val order = completedOrder()

        // Assert
        assertFailsWith<IllegalStateException> {
            // Act
            order.complete()
        }
    }
}
