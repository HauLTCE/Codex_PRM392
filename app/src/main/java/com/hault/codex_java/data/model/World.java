package com.hault.codex_java.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "worlds")
public class World {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String description;
    public long createdAt;
    public long lastModifiedAt;
    public String imageUri;
    public String tags;
    public boolean isPinned;
    public String colorHex;

    public World(String name, String description) {
        this.name = name;
        this.description = description;
        this.createdAt = System.currentTimeMillis();
        this.lastModifiedAt = System.currentTimeMillis();
        this.isPinned = false;
    }
}