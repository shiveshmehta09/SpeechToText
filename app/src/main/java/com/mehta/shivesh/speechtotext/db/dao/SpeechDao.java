package com.mehta.shivesh.speechtotext.db.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.mehta.shivesh.speechtotext.db.entity.GetSpeechEntity;

import java.util.List;

@Dao
public interface SpeechDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveDataToDb(List<GetSpeechEntity> data);

    @Query("SELECT * FROM table_data ORDER BY id ASC")
    LiveData<List<GetSpeechEntity>> loadDataFromDb();
}
