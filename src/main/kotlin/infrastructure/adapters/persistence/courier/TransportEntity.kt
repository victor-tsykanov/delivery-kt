package com.example.delivery.infrastructure.adapters.persistence.courier

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class TransportEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TransportEntity>(TransportTable)

    var name by TransportTable.name
    var speed by TransportTable.speed
    var createdAt by TransportTable.createdAt
    var updatedAt by TransportTable.updatedAt
}
