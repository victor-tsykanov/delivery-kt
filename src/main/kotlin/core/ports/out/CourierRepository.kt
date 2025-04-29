package com.example.delivery.core.ports.out

import com.example.delivery.core.domain.model.courier.Courier
import java.util.*

interface CourierRepository {
    suspend fun create(courier: Courier)
    suspend fun update(courier: Courier)
    suspend fun get(id: UUID): Courier
    suspend fun findFree(): List<Courier>
}
