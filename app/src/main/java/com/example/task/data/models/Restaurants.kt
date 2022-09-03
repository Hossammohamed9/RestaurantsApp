package com.example.task.data.models

data class Restaurants(
    val IsOpen: String,
    val RestauranthId: Int,
    val cover: String,
    val cuisines: List<CuisineXX>,
    val delivery_cost: Int,
    val delivery_time: Int,
    val logo: String,
    val name: String,
    val rate: Any
)