package com.example.delivery.infrastructure.adapters.persistence.order

import com.example.delivery.common.EntityExistsException
import com.example.delivery.common.EntityNotFoundException
import com.example.delivery.factories.CourierFactory
import com.example.delivery.factories.OrderFactory
import com.example.delivery.infrastructure.adapters.persistence.connectToDatabase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.junit.jupiter.api.assertInstanceOf
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderRepositoryImplTest {
    private val repository = OrderRepositoryImpl()
    private val db = connectToDatabase()

    @AfterTest
    fun cleanUpDB(): Unit = runBlocking {
        newSuspendedTransaction {
            OrderTable.deleteAll()
        }
    }

    @Test
    fun `create should add new order when it does not exist`() = runTest {
        newSuspendedTransaction(db = db) {
            // Arrange
            val order = OrderFactory.assignedOrder()

            // Act
            repository.create(order)

            // Assert
            val orderEntity = OrderEntity.get(order.id)
            assertEquals(order.id, orderEntity.id.value)
            assertEquals(order.status.value, orderEntity.status)
            assertEquals(order.deliveryLocation.x, orderEntity.deliveryLocationX)
            assertEquals(order.deliveryLocation.y, orderEntity.deliveryLocationY)
            assertEquals(order.courierId, orderEntity.courierId)
            assertNotNull(orderEntity.createdAt)
            assertNull(orderEntity.updatedAt)
        }
    }

    @Test
    fun `create should throw exception when order already exists`() = runBlocking {
        newSuspendedTransaction(db = db) {
            // Arrange
            val order = OrderFactory.assignedOrder()
            repository.create(order)

            // Act
            val exception = runCatching { runTest { repository.create(order) } }.exceptionOrNull()

            // Assert
            assertNotNull(exception)
            assertInstanceOf<EntityExistsException>(exception)
            assertEquals("Order with id=${order.id} already exists", exception.message)
        }
    }

    @Test
    fun `update should change order when it exists`() = runTest {
        newSuspendedTransaction(db = db) {
            // Arrange
            val order = OrderFactory.unassignedOrder()
            repository.create(order)
            order.assign(CourierFactory.freeCourier())

            // Act
            repository.update(order)

            // Assert
            val orderEntity = OrderEntity.get(order.id)
            assertEquals(order.id, orderEntity.id.value)
            assertEquals(order.status.value, orderEntity.status)
            assertEquals(order.deliveryLocation.x, orderEntity.deliveryLocationX)
            assertEquals(order.deliveryLocation.y, orderEntity.deliveryLocationY)
            assertEquals(order.courierId, orderEntity.courierId)
            assertNotNull(orderEntity.updatedAt)
        }
    }

    @Test
    fun `update should throw exception when order does not exist`() = runBlocking {
        newSuspendedTransaction(db = db) {
            val order = OrderFactory.assignedOrder()

            // Act
            val exception = runCatching { runTest { repository.update(order) } }.exceptionOrNull()

            // Assert
            assertNotNull(exception)
            assertInstanceOf<EntityNotFoundException>(exception)
            assertEquals("Order with id=${order.id} is not found", exception.message)
        }
    }

    @Test
    fun `get should return order when it exists`() = runTest {
        newSuspendedTransaction(db = db) {
            // Arrange
            val order = OrderFactory.assignedOrder()
            repository.create(order)

            // Act
            val orderFromDB = repository.get(order.id)

            // Assert
            assertEquals(order.id, orderFromDB.id)
            assertEquals(order.status, orderFromDB.status)
            assertEquals(order.deliveryLocation.x, orderFromDB.deliveryLocation.x)
            assertEquals(order.deliveryLocation.y, orderFromDB.deliveryLocation.y)
            assertEquals(order.courierId, orderFromDB.courierId)
        }
    }

    @Test
    fun `get should throw exception when order does not exist`() = runBlocking {
        newSuspendedTransaction {
            // Arrange
            val orderId = UUID.randomUUID()

            // Act
            val exception = runCatching { runTest { repository.get(orderId) } }.exceptionOrNull()

            // Assert
            assertNotNull(exception)
            assertInstanceOf<EntityNotFoundException>(exception)
            assertEquals("Order with id=${orderId} is not found", exception.message)
        }
    }

    @Test
    fun `findUnassigned should return list of orders with NEW status`() = runTest {
        newSuspendedTransaction(db = db) {
            // Arrange
            val orders = listOf(
                OrderFactory.unassignedOrder(),
                OrderFactory.assignedOrder(),
                OrderFactory.unassignedOrder(),
            )
            orders.forEach { repository.create(it) }

            // Act
            val ordersFromDB = repository.findUnassigned()

            // Assert
            assertEquals(
                listOf(orders[0], orders[2]),
                ordersFromDB
            )
        }
    }

    @Test
    fun `findAssigned should return list of orders with ASSIGNED status`() = runTest {
        newSuspendedTransaction(db = db) {
            // Arrange
            val orders = listOf(
                OrderFactory.assignedOrder(),
                OrderFactory.unassignedOrder(),
                OrderFactory.assignedOrder(),
            )
            orders.forEach { repository.create(it) }

            // Act
            val ordersFromDB = repository.findAssigned()

            // Assert
            assertEquals(
                listOf(orders[0], orders[2]),
                ordersFromDB
            )
        }
    }
}
