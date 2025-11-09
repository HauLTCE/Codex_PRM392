package com.hault.codex_java.data.model.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.hault.codex_java.data.model.Character;
import com.hault.codex_java.data.model.Event;
import com.hault.codex_java.data.model.Location;
import com.hault.codex_java.data.model.crossref.EventLocationCrossRef;

import java.util.List;

public class LocationWithDetails {
    @Embedded
    public Location location;

    @Relation(parentColumn = "id", entityColumn = "homeLocationId")
    public List<Character> residents;

    @Relation(
            parentColumn = "id",
            entity = Event.class,
            entityColumn = "id",
            associateBy = @Junction(
                    value = EventLocationCrossRef.class,
                    parentColumn = "locationId",
                    entityColumn = "eventId"
            )
    )
    public List<Event> events;
}