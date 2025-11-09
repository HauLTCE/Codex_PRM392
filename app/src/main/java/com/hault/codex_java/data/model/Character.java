package com.hault.codex_java.data.model;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "characters",
        foreignKeys = {
                @ForeignKey(
                        entity = World.class,
                        parentColumns = "id",
                        childColumns = "worldId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Location.class,
                        parentColumns = "id",
                        childColumns = "homeLocationId",
                        onDelete = ForeignKey.SET_NULL
                )
        },
        indices = {
                @Index(value = "worldId"),
                @Index(value = "homeLocationId")
        }
)
public class Character {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String backstory;
    public int worldId;
    public Integer homeLocationId;

    public Character(String name, String backstory, int worldId, Integer homeLocationId) {
        this.name = name;
        this.backstory = backstory;
        this.worldId = worldId;
        this.homeLocationId = homeLocationId;
    }
}