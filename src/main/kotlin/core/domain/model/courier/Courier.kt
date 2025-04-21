package com.example.delivery.core.domain.model.courier

import com.example.delivery.core.domain.model.shared.Location
import java.util.*

class Courier private constructor(
    val id: UUID,
    val name: String,
    val transport: Transport,
    location: Location,
    status: Status,
) {
    var location = location
        private set

    var status = status
        private set

    constructor(
        name: String,
        transportName: String,
        transportSpeed: Int,
        location: Location,
    ) : this(
        UUID.randomUUID(),
        name,
        Transport(UUID.randomUUID(), transportName, transportSpeed),
        location,
        Status.FREE,
    )

    fun setBusy() = when (status) {
        Status.FREE -> status = Status.BUSY
        Status.BUSY -> error("Courier must be free")
    }

    fun setFree() = when (status) {
        Status.BUSY -> status = Status.FREE
        Status.FREE -> error("Courier must be busy")
    }

    fun move(targetLocation: Location) {
        location = transport.move(location, targetLocation)
    }

    fun calculateStepsToLocation(targetLocation: Location): Int {
        var steps = 0
        var currentLocation = location

        while (true) {
            if (currentLocation == targetLocation) {
                break
            }

            currentLocation = transport.move(currentLocation, targetLocation)
            steps++
        }

        return steps
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Courier

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Courier(id=$id, name='$name', transport=$transport, location=$location, status=$status)"
    }

    companion object {
        fun restore(
            id: UUID,
            name: String,
            transport: Transport,
            location: Location,
            status: Status,
        ) = Courier(
            id,
            name,
            transport,
            location,
            status,
        )
    }

    enum class Status(val value: String) {
        FREE("Free"),
        BUSY("Busy")
    }
}
