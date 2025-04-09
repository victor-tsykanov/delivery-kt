package com.example.delivery.core.domain.model.order

import com.example.delivery.core.domain.model.courier.Courier
import com.example.delivery.core.domain.model.shared.Location
import java.util.UUID

class Order private constructor(
    val id: UUID,
    val deliveryLocation: Location,
    status: Status,
    courierId: UUID?,
) {
    var status = status
        private set

    var courierId = courierId
        private set

    constructor(id: UUID, deliveryLocation: Location) : this(id, deliveryLocation, Status.CREATED, null)

    fun assign(courier: Courier) = when (status) {
        Status.CREATED -> {
            courierId = courier.id
            status = Status.ASSIGNED
        }

        Status.ASSIGNED -> error("Order is already assigned")
        Status.COMPLETED -> error("Order is completed")
    }

    fun complete() = when (status) {
        Status.ASSIGNED -> {
            status = Status.COMPLETED
        }

        Status.CREATED -> error("Order needs to be assigned")
        Status.COMPLETED -> error("Order is already completed")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Order

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Order(id=$id, deliveryLocation=$deliveryLocation, status=$status, courierId=$courierId)"
    }

    companion object {
        fun restore(
            id: UUID,
            deliveryLocation: Location,
            status: Status,
            courierId: UUID?,
        ) = Order(
            id,
            deliveryLocation,
            status,
            courierId,
        )
    }

    enum class Status(val value: String) {
        CREATED("Created"),
        ASSIGNED("Assigned"),
        COMPLETED("Completed")
    }
}

