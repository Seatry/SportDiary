package com.example.alexander.sportdiary;

public enum MenuItemIds {
    EDIT_GROUP,
    EXERCISES,
    ZONES,
    AIMS,
    EQUIPMENTS,
    TIMES,
    TRAINING_PLACES,
    BORG_RATINGS,
    STYLES,
    TEMPOS,
    COMPETITIONS,
    IMPORTANCE,
    BLOCKS,
    STAGES,
    TYPES,
    CAMPS,
    OVERALL_PLAN,
    COMPETITION_SCHEDULE,
    STATISTICS,
    CALENDAR,
    DIARY_GROUP,
    ADD_DIARY,
    REST_PLACE,
    TEST;

    public int getValue() {
        int start = 10000;
        switch (this) {
            case EDIT_GROUP: return start;
            case EXERCISES: return start+1;
            case ZONES: return start+2;
            case AIMS: return start+3;
            case EQUIPMENTS: return start+4;
            case TIMES: return start+5;
            case TRAINING_PLACES: return start+6;
            case BORG_RATINGS: return start+7;
            case STYLES: return start+8;
            case TEMPOS: return start+9;
            case COMPETITIONS: return start+10;
            case IMPORTANCE: return start+11;
            case BLOCKS: return start+12;
            case STAGES: return start+13;
            case TYPES: return start+14;
            case CAMPS: return start+15;
            case ADD_DIARY: return start+16;
            case STATISTICS: return start+17;
            case DIARY_GROUP: return start+18;
            case OVERALL_PLAN: return start+19;
            case COMPETITION_SCHEDULE: return start+20;
            case CALENDAR: return start+21;
            case TEST: return start+22;
            case REST_PLACE: return start+23;
        }
        return -1;
    }
}
