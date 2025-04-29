package com.example.delivery.common

import java.util.UUID

class EntityNotFoundException(type: String, id: UUID) :
    RuntimeException("${type.replaceFirstChar { it.uppercase() }} with id=$id is not found")
