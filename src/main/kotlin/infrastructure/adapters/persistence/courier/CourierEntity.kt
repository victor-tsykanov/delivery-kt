package com.example.delivery.infrastructure.adapters.persistence.courier

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class CourierEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CourierEntity>(CourierTable)

    var name by CourierTable.name
    var locationX by CourierTable.locationX
    var locationY by CourierTable.locationY
    var status by CourierTable.status
    val transport by TransportEntity backReferencedOn TransportTable.courierId
    var createdAt by CourierTable.createdAt
    var updatedAt by CourierTable.updatedAt
}
