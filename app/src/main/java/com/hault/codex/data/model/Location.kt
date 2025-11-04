package com.hault.codex.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

import androidx.room.Index

@Entity(tableName = "locations",
    foreignKeys = [
        ForeignKey(entity = World::class,
            parentColumns = ["id"],
            childColumns = ["worldId"],
            onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["worldId"])]
)
data class Location(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String?,
    val worldId: Int
)