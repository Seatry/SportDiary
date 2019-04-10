package com.example.alexander.sportdiary.Enums;

public enum SanType {
    HEALTH,
    ACTIVITY,
    MOOD;

    public int getValue() {
        switch (this) {
            case HEALTH: return 0;
            case ACTIVITY: return 1;
            case MOOD: return 2;
        }
        return -1;
    }
}
