package com.example.delivery.infrastructure.adapters.persistence.courier

import com.example.delivery.common.EntityExistsException
import com.example.delivery.common.EntityNotFoundException
import com.example.delivery.core.domain.model.courier.Courier
import com.example.delivery.core.domain.model.courier.Transport
import com.example.delivery.core.domain.model.shared.Location
import com.example.delivery.core.ports.out.CourierRepository
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.util.*

class CourierRepositoryImpl : CourierRepository {
    override suspend fun create(courier: Courier): Unit = newSuspendedTransaction {
        if (exists(courier.id)) {
            throw EntityExistsException("courier", courier.id)
        }

        CourierTable.insert {
            it[id] = courier.id
            it[name] = courier.name
            it[locationX] = courier.location.x
            it[locationY] = courier.location.y
            it[status] = courier.status.value
            it[createdAt] = CurrentDateTime
        }
        TransportTable.insert {
            it[id] = courier.transport.id
            it[name] = courier.transport.name
            it[speed] = courier.transport.speed
            it[courierId] = courier.id
            it[createdAt] = CurrentDateTime
        }
    }

    override suspend fun update(courier: Courier) {
        if (!exists(courier.id)) {
            throw EntityNotFoundException("courier", courier.id)
        }

        CourierTable.update({ CourierTable.id eq courier.id }) {
            it[name] = courier.name
            it[locationX] = courier.location.x
            it[locationY] = courier.location.y
            it[status] = courier.status.value
            it[updatedAt] = CurrentDateTime
        }
        TransportTable.update({ TransportTable.id eq courier.transport.id }) {
            it[name] = courier.transport.name
            it[speed] = courier.transport.speed
            it[courierId] = courier.id
            it[updatedAt] = CurrentDateTime
        }
    }

    override suspend fun get(id: UUID) = newSuspendedTransaction {
        CourierEntity
            .findById(id)
            ?.run(::entityToCourier)
            ?: throw EntityNotFoundException("courier", id)
    }

    override suspend fun findFree() = newSuspendedTransaction {
        CourierEntity
            .find { CourierTable.status eq Courier.Status.FREE.value }
            .with(CourierEntity::transport)
            .orderBy(CourierTable.createdAt to SortOrder.ASC)
            .map(::entityToCourier)
    }

    private fun entityToCourier(entity: CourierEntity) = Courier.restore(
        id = entity.id.value,
        name = entity.name,
        transport = Transport(
            id = entity.transport.id.value,
            name = entity.transport.name,
            speed = entity.transport.speed
        ),
        location = Location(
            x = entity.locationX,
            y = entity.locationY
        ),
        status = Courier.Status.of(entity.status)
    )

    private fun exists(id: UUID) = CourierEntity.findById(id) != null
}
