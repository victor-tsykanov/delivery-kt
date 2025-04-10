package com.example.delivery.factories

import com.example.delivery.core.domain.model.courier.Courier
import com.example.delivery.core.domain.model.shared.Location
import com.example.delivery.factories.LocationFactory.randomLocation
import net.datafaker.Faker

object CourierFactory {
    private val faker = Faker()

    fun freeCourierAtLocationWithSpeed(x: Int, y: Int, speed: Int) = Courier(
        name = faker.name().fullName(),
        transportName = faker.transport().type(),
        transportSpeed = speed,
        location = Location(x, y)
    )

    fun freeCourier() = Courier(
        name = faker.name().fullName(),
        transportName = faker.transport().type(),
        transportSpeed = faker.number().numberBetween(1, 4),
        location = randomLocation()
    )

    fun busyCourier() = freeCourier().apply { setBusy() }
}
