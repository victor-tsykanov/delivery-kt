package com.example.delivery.core.domain.model.shared

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LocationTest {
    @ParameterizedTest
    @CsvSource(
        value = [
            "1,1",
            "3,5",
            "10,10",
        ],
    )
    fun `constructor should create instance when arguments are valid`(x: Int, y: Int) {
        val location = Location(x,y)

        assertEquals(x, location.x)
        assertEquals(y, location.y)
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "0,1",
            "-1,1",
            "11,1",
            "1,0",
            "1,-1",
            "1,11",
        ],
    )
    fun `constructor should throw exception when arguments are not valid`(x: Int, y: Int) {
        assertFailsWith<IllegalArgumentException> {
            Location(x, y)
        }
    }
}
