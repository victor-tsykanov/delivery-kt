package com.example.delivery.infrastructure.adapters.persistence.order

import com.example.delivery.common.EntityExistsException
import com.example.delivery.common.EntityNotFoundException
import com.example.delivery.core.domain.model.order.Order
import com.example.delivery.core.domain.model.shared.Location
import com.example.delivery.core.ports.out.OrderRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.util.*


class OrderRepositoryImpl : OrderRepository {
    override suspend fun create(order: Order): Unit = newSuspendedTransaction {
        if (exists(order.id)) {
            throw EntityExistsException("order", order.id)
        }

        OrderTable.insert {
            it[id] = order.id
            it[deliveryLocationX] = order.deliveryLocation.x
            it[deliveryLocationY] = order.deliveryLocation.y
            it[status] = order.status.value
            it[courierId] = order.courierId
            it[createdAt] = CurrentDateTime
        }
    }

    override suspend fun update(order: Order): Unit = newSuspendedTransaction {
        if (!exists(order.id)) {
            throw EntityNotFoundException("order", order.id)
        }

        OrderTable.update({ OrderTable.id eq order.id }) {
            it[deliveryLocationX] = order.deliveryLocation.x
            it[deliveryLocationY] = order.deliveryLocation.y
            it[status] = order.status.value
            it[courierId] = order.courierId
            it[updatedAt] = CurrentDateTime
        }
    }

    override suspend fun get(id: UUID) = newSuspendedTransaction {
        OrderEntity
            .findById(id)
            ?.run(::entityToOrder)
            ?: throw EntityNotFoundException("order", id)
    }

    override suspend fun findUnassigned() = newSuspendedTransaction {
        OrderEntity
            .find { OrderTable.status eq Order.Status.CREATED.value }
            .orderBy(OrderTable.createdAt to SortOrder.ASC)
            .map(::entityToOrder)
    }

    override suspend fun findAssigned() = newSuspendedTransaction {
        OrderEntity
            .find { OrderTable.status eq Order.Status.ASSIGNED.value }
            .orderBy(OrderTable.createdAt to SortOrder.ASC)
            .map(::entityToOrder)
    }

    private fun entityToOrder(entity: OrderEntity) = Order.restore(
        id = entity.id.value,
        deliveryLocation = Location(entity.deliveryLocationX, entity.deliveryLocationY),
        status = Order.Status.of(entity.status),
        courierId = entity.courierId,
    )

    private fun exists(id: UUID) = OrderEntity.findById(id) != null
}
