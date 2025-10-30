package com.username.codex.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "characters",
    foreignKeys = [
        ForeignKey(
            entity = World::class,
            parentColumns = ["id"],
            childColumns = ["worldId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["worldId"])]
)
data class Character(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val backstory: String?,
    val worldId: Int
)
