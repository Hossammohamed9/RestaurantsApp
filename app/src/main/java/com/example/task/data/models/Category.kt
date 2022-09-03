package com.example.task.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: Int,
    val name: String,
    val name_en: String,
    val percentage: Int,
    val photo: String
) : Parcelable