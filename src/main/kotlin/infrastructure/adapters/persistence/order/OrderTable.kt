package com.example.delivery.infrastructure.adapters.persistence.order

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object OrderTable : UUIDTable("orders") {
    val deliveryLocationX = integer("delivery_location_x")
    val deliveryLocationY = integer("delivery_location_y")
    val status = varchar("status", 100)
    val courierId = uuid("courier_id").nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at").nullable()
}
