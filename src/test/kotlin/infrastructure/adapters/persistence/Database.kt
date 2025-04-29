package com.example.delivery.infrastructure.adapters.persistence

import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database

fun connectToDatabase(): Database {
    val dotenv = dotenv()

    return Database.Companion.connect(
        url = "jdbc:postgresql://${dotenv["DB_HOST"]}:${dotenv["DB_PORT"]}/${dotenv["DB_DATABASE"]}",
        driver = "org.postgresql.Driver",
        user = dotenv["DB_USER"],
        password = dotenv["DB_PASSWORD"],
    )
}
