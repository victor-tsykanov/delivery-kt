package com.example.delivery.core.domain.services

import com.example.delivery.core.domain.model.courier.Courier
import com.example.delivery.core.domain.model.order.Order

interface DispatchService {
    fun dispatch(order: Order, couriers: List<Courier>): Courier
}
