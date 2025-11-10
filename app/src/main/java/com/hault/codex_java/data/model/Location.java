package com.hault.codex_java.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "locations",
        foreignKeys = @ForeignKey(
                entity = World.class,
                parentColumns = "id",
                childColumns = "worldId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(value = "worldId")}
)
public class Location {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String description;
    public int worldId;
    public long createdAt;
    public long lastModifiedAt;
    public String imageUri;
    public String tags;
    public boolean isPinned;
    public String colorHex;

    public Location(String name, String description, int worldId) {
        this.name = name;
        this.description = description;
        this.worldId = worldId;
        this.createdAt = System.currentTimeMillis();
        this.lastModifiedAt = System.currentTimeMillis();
        this.isPinned = false;
    }
}