package com.hault.codex_java.data.model.crossref;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.hault.codex_java.data.model.Character;

@Entity(
        tableName = "character_character_cross_ref",
        primaryKeys = {"characterOneId", "characterTwoId"},
        foreignKeys = {
                @ForeignKey(
                        entity = com.hault.codex_java.data.model.Character.class,
                        parentColumns = "id",
                        childColumns = "characterOneId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Character.class,
                        parentColumns = "id",
                        childColumns = "characterTwoId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("characterOneId"),
                @Index("characterTwoId")
        }
)
public class CharacterCharacterCrossRef {
    public int characterOneId;
    public int characterTwoId;
    public String relationshipDescription;

    public CharacterCharacterCrossRef(int characterOneId, int characterTwoId, String relationshipDescription) {
        this.characterOneId = characterOneId;
        this.characterTwoId = characterTwoId;
        this.relationshipDescription = relationshipDescription;
    }
}