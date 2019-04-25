package com.example.alexander.sportdiary.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = "userId", unique = true)})
public class Version {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private Long version = 0L;

    public Version(String userId, Long version) {
        this.userId = userId;
        this.version = version;
    }

    public Version() {

    }

    private String userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
