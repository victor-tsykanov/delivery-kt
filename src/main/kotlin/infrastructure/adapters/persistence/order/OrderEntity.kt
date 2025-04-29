package com.example.delivery.infrastructure.adapters.persistence.order

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class OrderEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<OrderEntity>(OrderTable)

    var deliveryLocationX by OrderTable.deliveryLocationX
    var deliveryLocationY by OrderTable.deliveryLocationY
    var status by OrderTable.status
    var courierId by OrderTable.courierId
    var createdAt by OrderTable.createdAt
    var updatedAt by OrderTable.updatedAt
}
