package com.hault.codex.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "worlds")
data class World(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String?
)
