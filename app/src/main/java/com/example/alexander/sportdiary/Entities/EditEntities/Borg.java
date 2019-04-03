package com.example.alexander.sportdiary.Entities.EditEntities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

@Entity(indices = {@Index(value = "name", unique = true)})
public class Borg extends Edit{
    public Borg(String name) {
        super(name);
    }

    public Borg() {

    }
}
