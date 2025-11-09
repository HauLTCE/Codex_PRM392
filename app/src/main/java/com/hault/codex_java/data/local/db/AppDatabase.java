package com.hault.codex_java.data.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.migration.Migration;

import com.hault.codex_java.data.local.dao.ArcDao;
import com.hault.codex_java.data.local.dao.CharacterDao;
import com.hault.codex_java.data.local.dao.ChapterDao;
import com.hault.codex_java.data.local.dao.CrossRefDao;
import com.hault.codex_java.data.local.dao.EventDao;
import com.hault.codex_java.data.local.dao.LocationDao;
import com.hault.codex_java.data.local.dao.WorldDao;
import com.hault.codex_java.data.model.Arc;
import com.hault.codex_java.data.model.Character;
import com.hault.codex_java.data.model.Chapter;
import com.hault.codex_java.data.model.Event;
import com.hault.codex_java.data.model.Location;
import com.hault.codex_java.data.model.World;
import com.hault.codex_java.data.model.crossref.ChapterCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.ChapterEventCrossRef;
import com.hault.codex_java.data.model.crossref.ChapterLocationCrossRef;
import com.hault.codex_java.data.model.crossref.CharacterCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.EventCharacterCrossRef;
import com.hault.codex_java.data.model.crossref.EventLocationCrossRef;


@Database(entities = {
        World.class, Character.class, Location.class, Event.class, Arc.class, Chapter.class,
        CharacterCharacterCrossRef.class, EventCharacterCrossRef.class, EventLocationCrossRef.class,
        ChapterCharacterCrossRef.class, ChapterLocationCrossRef.class, ChapterEventCrossRef.class
}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WorldDao worldDao();
    public abstract CharacterDao characterDao();
    public abstract LocationDao locationDao();
    public abstract EventDao eventDao();
    public abstract ArcDao arcDao();
    public abstract ChapterDao chapterDao();
    public abstract CrossRefDao crossRefDao();

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS events (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, description TEXT, date TEXT NOT NULL, worldId INTEGER NOT NULL, FOREIGN KEY(worldId) REFERENCES worlds(id) ON UPDATE NO ACTION ON DELETE CASCADE)");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_events_worldId ON events(worldId)");
        }
    };

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create Arcs and Chapters tables
            database.execSQL("CREATE TABLE IF NOT EXISTS `arcs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `worldId` INTEGER NOT NULL, `title` TEXT, `synopsis` TEXT, `displayOrder` INTEGER NOT NULL, FOREIGN KEY(`worldId`) REFERENCES `worlds`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_arcs_worldId` ON `arcs` (`worldId`)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `chapters` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `arcId` INTEGER NOT NULL, `worldId` INTEGER NOT NULL, `title` TEXT, `chapterNumber` INTEGER NOT NULL, `prose` TEXT, FOREIGN KEY(`worldId`) REFERENCES `worlds`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`arcId`) REFERENCES `arcs`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_chapters_worldId` ON `chapters` (`worldId`)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_chapters_arcId` ON `chapters` (`arcId`)");

            // Create Cross-Reference tables
            database.execSQL("CREATE TABLE IF NOT EXISTS `character_character_cross_ref` (`characterOneId` INTEGER NOT NULL, `characterTwoId` INTEGER NOT NULL, `relationshipDescription` TEXT, PRIMARY KEY(`characterOneId`, `characterTwoId`), FOREIGN KEY(`characterOneId`) REFERENCES `characters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`characterTwoId`) REFERENCES `characters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `event_character_cross_ref` (`eventId` INTEGER NOT NULL, `characterId` INTEGER NOT NULL, `roleInEvent` TEXT, PRIMARY KEY(`eventId`, `characterId`), FOREIGN KEY(`eventId`) REFERENCES `events`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`characterId`) REFERENCES `characters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `event_location_cross_ref` (`eventId` INTEGER NOT NULL, `locationId` INTEGER NOT NULL, PRIMARY KEY(`eventId`, `locationId`), FOREIGN KEY(`eventId`) REFERENCES `events`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`locationId`) REFERENCES `locations`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `chapter_character_cross_ref` (`chapterId` INTEGER NOT NULL, `characterId` INTEGER NOT NULL, `roleInChapter` TEXT, PRIMARY KEY(`chapterId`, `characterId`), FOREIGN KEY(`chapterId`) REFERENCES `chapters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`characterId`) REFERENCES `characters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `chapter_location_cross_ref` (`chapterId` INTEGER NOT NULL, `locationId` INTEGER NOT NULL, `roleInChapter` TEXT, PRIMARY KEY(`chapterId`, `locationId`), FOREIGN KEY(`chapterId`) REFERENCES `chapters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`locationId`) REFERENCES `locations`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `chapter_event_cross_ref` (`chapterId` INTEGER NOT NULL, `eventId` INTEGER NOT NULL, PRIMARY KEY(`chapterId`, `eventId`), FOREIGN KEY(`chapterId`) REFERENCES `chapters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE, FOREIGN KEY(`eventId`) REFERENCES `events`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE)");
        }
    };

    public static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Add new columns to Worlds table
            database.execSQL("ALTER TABLE worlds ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE worlds ADD COLUMN lastModifiedAt INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE worlds ADD COLUMN imageUri TEXT");
            database.execSQL("ALTER TABLE worlds ADD COLUMN tags TEXT");
            database.execSQL("ALTER TABLE worlds ADD COLUMN isPinned INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE worlds ADD COLUMN colorHex TEXT");

            // Add new columns to Characters table
            database.execSQL("ALTER TABLE characters ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE characters ADD COLUMN lastModifiedAt INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE characters ADD COLUMN imageUri TEXT");
            database.execSQL("ALTER TABLE characters ADD COLUMN tags TEXT");
            database.execSQL("ALTER TABLE characters ADD COLUMN isPinned INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE characters ADD COLUMN colorHex TEXT");

            // Add new columns to Locations table
            database.execSQL("ALTER TABLE locations ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE locations ADD COLUMN lastModifiedAt INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE locations ADD COLUMN imageUri TEXT");
            database.execSQL("ALTER TABLE locations ADD COLUMN tags TEXT");
            database.execSQL("ALTER TABLE locations ADD COLUMN isPinned INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE locations ADD COLUMN colorHex TEXT");

            // Add new columns to Events table
            database.execSQL("ALTER TABLE events ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE events ADD COLUMN lastModifiedAt INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE events ADD COLUMN imageUri TEXT");
            database.execSQL("ALTER TABLE events ADD COLUMN tags TEXT");
            database.execSQL("ALTER TABLE events ADD COLUMN isPinned INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE events ADD COLUMN colorHex TEXT");

            // Add new columns to Arcs table
            database.execSQL("ALTER TABLE arcs ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE arcs ADD COLUMN lastModifiedAt INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE arcs ADD COLUMN imageUri TEXT");
            database.execSQL("ALTER TABLE arcs ADD COLUMN tags TEXT");
            database.execSQL("ALTER TABLE arcs ADD COLUMN isPinned INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE arcs ADD COLUMN colorHex TEXT");

            // Add new columns to Chapters table
            database.execSQL("ALTER TABLE chapters ADD COLUMN wordCount INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE chapters ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE chapters ADD COLUMN lastModifiedAt INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE chapters ADD COLUMN imageUri TEXT");
            database.execSQL("ALTER TABLE chapters ADD COLUMN tags TEXT");
            database.execSQL("ALTER TABLE chapters ADD COLUMN isPinned INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE chapters ADD COLUMN colorHex TEXT");
        }
    };
}