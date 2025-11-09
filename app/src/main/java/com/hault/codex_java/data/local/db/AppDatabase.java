package com.hault.codex_java.data.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.migration.Migration;
import com.hault.codex_java.data.local.dao.CharacterDao;
import com.hault.codex_java.data.local.dao.EventDao;
import com.hault.codex_java.data.local.dao.LocationDao;
import com.hault.codex_java.data.local.dao.WorldDao;
import com.hault.codex_java.data.model.Character;
import com.hault.codex_java.data.model.Event;
import com.hault.codex_java.data.model.Location;
import com.hault.codex_java.data.model.World;

@Database(entities = {World.class, Character.class, Location.class, Event.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WorldDao worldDao();
    public abstract CharacterDao characterDao();
    public abstract LocationDao locationDao();
    public abstract EventDao eventDao();

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS events (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, description TEXT, date TEXT NOT NULL, worldId INTEGER NOT NULL, FOREIGN KEY(worldId) REFERENCES worlds(id) ON UPDATE NO ACTION ON DELETE CASCADE)");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_events_worldId ON events(worldId)");
        }
    };
}