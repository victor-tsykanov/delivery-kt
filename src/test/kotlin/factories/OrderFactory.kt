package factories

import com.example.delivery.core.domain.model.order.Order
import factories.CourierFactory.freeCourier
import factories.LocationFactory.randomLocation
import java.util.*

object OrderFactory {
    fun unassignedOrder() = Order(UUID.randomUUID(), randomLocation())

    fun assignedOrder() = unassignedOrder().apply { assign(freeCourier()) }

    fun completedOrder() = assignedOrder().apply { complete() }
}
