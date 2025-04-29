package com.example.delivery.common

import java.util.UUID

class EntityExistsException(type: String, id: UUID) :
    RuntimeException("${type.replaceFirstChar { it.uppercase() }} with id=$id already exists")
