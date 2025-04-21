package com.example.delivery.core.domain.model.courier

import com.example.delivery.core.domain.model.shared.Location
import org.junit.jupiter.api.Named.named
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TransportTest {
    @Test
    fun `constructor should create instance when arguments are valid`() {
        val transport = Transport(UUID.randomUUID(), "Car", 3)

        assertEquals("Car", transport.name)
        assertEquals(3, transport.speed)
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "'',2",
            "' ',2",
            "Car,4",
            "Car,0",
            "Car,-1",
        ],
    )
    fun `constructor should throw exception when arguments are not valid`(name: String, speed: Int) {
        assertFailsWith<IllegalArgumentException> {
            Transport(UUID.randomUUID(), name, speed)
        }
    }

    @ParameterizedTest
    @MethodSource("moveTestCases")
    fun `move should return next location`(case: MoveTestCase) {
        val transport = Transport(UUID.randomUUID(), "...", 3)

        val nextLocation = transport.move(case.from, case.to)

        assertEquals(case.expected, nextLocation)
    }

    class MoveTestCase(
        var name: String,
        var from: Location,
        var to: Location,
        var expected: Location,
    )

    companion object {
        @JvmStatic
        fun moveTestCases() = listOf(
            MoveTestCase(
                name = "move ↘️ within the range with remaining",
                from = Location(1, 1),
                to = Location(2, 2),
                expected = Location(2, 2),
            ),
            MoveTestCase(
                name = "move ↖️ within the range with remaining",
                from = Location(2, 2),
                to = Location(1, 1),
                expected = Location(1, 1),
            ),
            MoveTestCase(
                name = "move ↘️ within the range without remaining",
                from = Location(1, 1),
                to = Location(2, 3),
                expected = Location(2, 3),
            ),
            MoveTestCase(
                name = "move ↖️ within the range without remaining",
                from = Location(2, 3),
                to = Location(1, 1),
                expected = Location(1, 1),
            ),
            MoveTestCase(
                name = "move ↘️ beyond the range only along X",
                from = Location(1, 1),
                to = Location(5, 5),
                expected = Location(4, 1),
            ),
            MoveTestCase(
                name = "move ↖️ beyond the range only along X",
                from = Location(5, 5),
                to = Location(1, 1),
                expected = Location(2, 5),
            ),
            MoveTestCase(
                name = "move ↘️ beyond the range along X and Y",
                from = Location(1, 1),
                to = Location(3, 7),
                expected = Location(3, 2),
            ),
            MoveTestCase(
                name = "move ↖️ beyond the range along X and Y",
                from = Location(3, 7),
                to = Location(1, 1),
                expected = Location(1, 6),
            ),
            MoveTestCase(
                name = "move to the same location",
                from = Location(1, 1),
                to = Location(1, 1),
                expected = Location(1, 1),
            ),
        ).map {
            arguments(named(it.name, it))
        }
    }
}
