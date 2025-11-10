package com.hault.codex_java.data.model.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.hault.codex_java.data.model.Character;
import com.hault.codex_java.data.model.Event;
import com.hault.codex_java.data.model.Location;
import com.hault.codex_java.data.model.crossref.EventCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.EventLocationCrossRef;

import java.util.List;

public class EventWithDetails {
    @Embedded
    public Event event;

    @Relation(
            parentColumn = "id",
            entity = Character.class,
            entityColumn = "id",
            associateBy = @Junction(
                    value = EventCharacterCrossRef.class,
                    parentColumn = "eventId",
                    entityColumn = "characterId"
            )
    )
    public List<Character> characters;

    @Relation(
            parentColumn = "id",
            entity = Location.class,
            entityColumn = "id",
            associateBy = @Junction(
                    value = EventLocationCrossRef.class,
                    parentColumn = "eventId",
                    entityColumn = "locationId"
            )
    )
    public List<Location> locations;
}