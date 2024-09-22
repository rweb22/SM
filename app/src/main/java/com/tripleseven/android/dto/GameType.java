package com.tripleseven.android.dto;

public enum GameType {
    HARF("HARF", "Harf"),
    SINGLE("SINGLE", "Single"),
    JODI("JODI", "Jodi"),
    SP("SP", "SP"),
    DP("DP", "DP"),
    TP("TP", "TP"),
    HALF_SANGAM("HALF_SANGAM", "Half Sangam"),
    FULL_SANGAM("HALF_SANGAM", "Half Sangam");

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    private final String id;
    private final String displayName;

    GameType(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }
}
