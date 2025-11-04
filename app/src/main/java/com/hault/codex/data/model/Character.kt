package com.hault.codex.data.model

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
        ),
        ForeignKey(
            entity = Location::class,
            parentColumns = ["id"],
            childColumns = ["homeLocationId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["worldId"]), Index(value = ["homeLocationId"])]
)
data class Character(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val backstory: String?,
    val worldId: Int,
    val homeLocationId: Int? = null
)
