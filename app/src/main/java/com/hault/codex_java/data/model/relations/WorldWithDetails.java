package com.hault.codex_java.data.model.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.hault.codex_java.data.model.Arc;
import com.hault.codex_java.data.model.Character;
import com.hault.codex_java.data.model.Event;
import com.hault.codex_java.data.model.Location;
import com.hault.codex_java.data.model.World;

import java.util.List;

public class WorldWithDetails {
    @Embedded
    public World world;

    @Relation(parentColumn = "id", entityColumn = "worldId")
    public List<Character> characters;

    @Relation(parentColumn = "id", entityColumn = "worldId")
    public List<Location> locations;

    @Relation(parentColumn = "id", entityColumn = "worldId")
    public List<Event> events;

    @Relation(parentColumn = "id", entityColumn = "worldId")
    public List<Arc> arcs;
}