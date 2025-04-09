package com.example.delivery.core.domain.services

import com.example.delivery.core.domain.model.courier.Courier
import com.example.delivery.core.domain.model.order.Order

object DispatchServiceImpl : DispatchService {
    override fun dispatch(order: Order, couriers: List<Courier>) = requireNotNull(
        couriers.minByOrNull { it.calculateStepsToLocation(order.deliveryLocation) },
        { "Couriers list must not be empty" }
    )
}
