package com.mehta.shivesh.speechtotext.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.mehta.shivesh.speechtotext.db.dao.SpeechDao;
import com.mehta.shivesh.speechtotext.db.entity.GetSpeechEntity;

@Database(entities = GetSpeechEntity.class, version = 1, exportSchema = false)
public abstract class AppDb extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile AppDb INSTANCE;

    // --- DAO ---
    public abstract SpeechDao speechDao();
}
