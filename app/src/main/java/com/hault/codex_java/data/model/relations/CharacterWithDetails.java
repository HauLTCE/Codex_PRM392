package com.hault.codex_java.data.model.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.hault.codex_java.data.model.Character;
import com.hault.codex_java.data.model.Event;
import com.hault.codex_java.data.model.Location;
import com.hault.codex_java.data.model.crossref.CharacterCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.EventCharacterCrossRef;

import java.util.List;

public class CharacterWithDetails {
    @Embedded
    public Character character;

    @Relation(parentColumn = "homeLocationId", entityColumn = "id")
    public Location homeLocation;

    @Relation(
            parentColumn = "id",
            entity = Event.class,
            entityColumn = "id",
            associateBy = @Junction(
                    value = EventCharacterCrossRef.class,
                    parentColumn = "characterId",
                    entityColumn = "eventId"
            )
    )
    public List<Event> events;

    @Relation(
            parentColumn = "id",
            entity = Character.class,
            entityColumn = "id",
            associateBy = @Junction(
                    value = CharacterCharacterCrossRef.class,
                    parentColumn = "characterOneId",
                    entityColumn = "characterTwoId"
            )
    )
    public List<Character> relatedCharacters;
}