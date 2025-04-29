package com.example.delivery.infrastructure.adapters.persistence.courier

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object TransportTable : UUIDTable("transports") {
    val name = varchar("name", 500)
    val speed = integer("speed")
    val courierId = uuid("courier_id").references(CourierTable.id, onDelete = ReferenceOption.CASCADE)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at").nullable()
}
