package com.example.delivery.factories

import com.example.delivery.core.domain.model.shared.Location
import net.datafaker.Faker

object LocationFactory {
    private val faker = Faker()

    fun randomLocation() = Location(
        x = faker.number().numberBetween(1, 11),
        y = faker.number().numberBetween(1, 11),
    )
}
