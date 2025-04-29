package com.example.delivery.core.ports.out

import com.example.delivery.core.domain.model.order.Order
import java.util.UUID

interface OrderRepository {
    suspend fun create(order: Order)
    suspend fun update(order: Order)
    suspend fun get(id: UUID): Order
    suspend fun findUnassigned(): List<Order>
    suspend fun findAssigned(): List<Order>
}
