package com.example.alexander.sportdiary.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.example.alexander.sportdiary.Entities.Version;

@Dao
public interface VersionDao {
    @Insert
    long insert(Version version);

    @Update
    void update(Version version);

    @Delete
    void delete(Version version);

    @Query("SELECT * FROM Version WHERE userId = :userId")
    Version getByUserId(String userId);
}
