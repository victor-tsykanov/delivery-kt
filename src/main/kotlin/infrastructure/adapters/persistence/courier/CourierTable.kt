package com.example.delivery.infrastructure.adapters.persistence.courier

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object CourierTable : UUIDTable("couriers") {
    val name = varchar("name", 500)
    val locationX = integer("location_x")
    val locationY = integer("location_y")
    val status = varchar("status", 100)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at").nullable()
}
