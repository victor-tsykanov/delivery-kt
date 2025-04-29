package com.example.delivery.infrastructure.adapters.persistence.courier

import com.example.delivery.common.EntityExistsException
import com.example.delivery.common.EntityNotFoundException
import com.example.delivery.core.domain.model.shared.Location
import com.example.delivery.factories.CourierFactory
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

class CourierRepositoryImplTest {
    private val repository = CourierRepositoryImpl()
    private val db = connectToDatabase()

    @AfterTest
    fun cleanUpDB(): Unit = runBlocking {
        newSuspendedTransaction {
            TransportTable.deleteAll()
            CourierTable.deleteAll()
        }
    }

    @Test
    fun `create should add new courier when it does not exist`() = runTest {
        newSuspendedTransaction(db = db) {
            // Arrange
            val courier = CourierFactory.freeCourier()

            // Act
            repository.create(courier)

            // Assert
            val courierEntity = CourierEntity.Companion.get(courier.id)
            assertEquals(courier.id, courierEntity.id.value)
            assertEquals(courier.name, courierEntity.name)
            assertEquals(courier.location.x, courierEntity.locationX)
            assertEquals(courier.location.y, courierEntity.locationY)
            assertEquals(courier.status.value, courierEntity.status)
            assertNotNull(courierEntity.createdAt)
            assertNull(courierEntity.updatedAt)
            assertEquals(courier.transport.id, courierEntity.transport.id.value)
            assertEquals(courier.transport.name, courierEntity.transport.name)
            assertEquals(courier.transport.speed, courierEntity.transport.speed)
            assertNotNull(courierEntity.transport.createdAt)
            assertNull(courierEntity.transport.updatedAt)
        }
    }

    @Test
    fun `create should throw exception when courier already exists`() = runBlocking {
        newSuspendedTransaction(db = db) {
            val courier = CourierFactory.freeCourier()
            repository.create(courier)

            // Act
            val exception = runCatching { runTest { repository.create(courier) } }.exceptionOrNull()

            // Assert
            assertNotNull(exception)
            assertInstanceOf<EntityExistsException>(exception)
            assertEquals("Courier with id=${courier.id} already exists", exception.message)
        }
    }

    @Test
    fun `update should change courier when courier exists`() = runTest {
        newSuspendedTransaction(db = db) {
            // Arrange
            val courier = CourierFactory.freeCourierAtLocationWithSpeed(5, 1, 2)
            repository.create(courier)
            courier.move(Location(2, 4))
            courier.setBusy()

            // Act
            repository.update(courier)

            // Assert
            val courierEntity = CourierEntity.Companion.get(courier.id)
            assertEquals(courier.id, courierEntity.id.value)
            assertEquals(courier.name, courierEntity.name)
            assertEquals(courier.location.x, courierEntity.locationX)
            assertEquals(courier.location.y, courierEntity.locationY)
            assertEquals(courier.status.value, courierEntity.status)
            assertNotNull(courierEntity.updatedAt)
            assertEquals(courier.transport.id, courierEntity.transport.id.value)
            assertEquals(courier.transport.name, courierEntity.transport.name)
            assertEquals(courier.transport.speed, courierEntity.transport.speed)
            assertNotNull(courierEntity.transport.updatedAt)
        }
    }

    @Test
    fun `update should throw exception when courier does not exist`() = runBlocking {
        newSuspendedTransaction(db = db) {
            val courier = CourierFactory.freeCourier()

            // Act
            val exception = runCatching { runTest { repository.update(courier) } }.exceptionOrNull()

            // Assert
            assertNotNull(exception)
            assertInstanceOf<EntityNotFoundException>(exception)
            assertEquals("Courier with id=${courier.id} is not found", exception.message)
        }
    }

    @Test
    fun `get should return courier when it exists`() = runTest {
        newSuspendedTransaction(db = db) {
            // Arrange
            val courier = CourierFactory.freeCourier()
            repository.create(courier)

            // Act
            val courierFromDB = repository.get(courier.id)

            // Assert
            assertEquals(courier.id, courierFromDB.id)
            assertEquals(courier.name, courierFromDB.name)
            assertEquals(courier.location.x, courierFromDB.location.x)
            assertEquals(courier.location.y, courierFromDB.location.y)
            assertEquals(courier.status, courierFromDB.status)
            assertEquals(courier.transport.id, courierFromDB.transport.id)
            assertEquals(courier.transport.name, courierFromDB.transport.name)
            assertEquals(courier.transport.speed, courierFromDB.transport.speed)
        }
    }

    @Test
    fun `get should throw exception when courier does not exist`() = runBlocking {
        newSuspendedTransaction {
            // Arrange
            val courierId = UUID.randomUUID()

            // Act
            val exception = runCatching { runTest { repository.get(courierId) } }.exceptionOrNull()

            // Assert
            assertNotNull(exception)
            assertInstanceOf<EntityNotFoundException>(exception)
            assertEquals("Courier with id=${courierId} is not found", exception.message)
        }
    }

    @Test
    fun `findFree should return list of couriers with FREE status`() = runTest {
        newSuspendedTransaction(db = db) {
            // Arrange
            val couriers = listOf(
                CourierFactory.freeCourier(),
                CourierFactory.busyCourier(),
                CourierFactory.freeCourier(),
            )
            couriers.forEach { repository.create(it) }

            // Act
            val couriersFromDB = repository.findFree()

            // Assert
            assertEquals(
                listOf(couriers[0], couriers[2]),
                couriersFromDB
            )
        }
    }
}
