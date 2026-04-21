package com.pascal.catalog.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val email: String,
    val username: String,
    val name: NameDto,
    val address: AddressDto,
    val phone: String,
)

@Serializable
data class NameDto(
    val firstname: String,
    val lastname: String,
)

@Serializable
data class AddressDto(
    val city: String,
    val street: String,
    val number: Int,
    val zipcode: String,
)
