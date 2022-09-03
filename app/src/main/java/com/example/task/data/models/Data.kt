package com.example.task.data.models

data class Data(
    val IsOpen: String,
    val RestauranthId: Int,
    val cover: String,
    val cuisines: List<Cuisine>,
    val delivery_cost: Int,
    val delivery_time: Int,
    val description: Any,
    val logo: String,
    val name: String,
    val rate: Any
)