package com.example.alexander.sportdiary.Entities.EditEntities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

@Entity(indices = {@Index(value = {"name", "userId"}, unique = true)})
public class Zone extends Edit {
    public Zone(String name) {
        super(name);
    }

    public Zone() {

    }

}

