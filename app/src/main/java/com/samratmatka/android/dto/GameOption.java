package com.samratmatka.android.dto;

public enum GameOption {
    SINGLE("SINGLE",  "Single"),
    JODI("JODI",  "Jodi"),
    SP("SP", "SP"),
    DP("DP",  "DP"),
    TP("TP",  "TP"),
    HALF_SANGAM("HALF_SANGAM","Half Sangam"),
    FULL_SANGAM("FULL_SANGAM", "Full Sangam"),
    SP_MOTOR("SP_MOTOR", "SP Motor"),
    DP_MOTOR("DP_MOTOR", "DP Motor"),
    SP_DP_TP("SP_DP_TP", "Panna Motor"),
    GROUP_JODI("GROUP_JODI", "Jodi Family"),
    JODI_MOTOR("JODI_MOTOR", "Crossing Jodi"),
    ODD_EVEN("ODD_EVEN", "Odd/Even"),
    RED_BRACKET("RED_BRACKET", "Red Bracket"),
    CHOICE_PANA("CHOICE_PANA", "Choice Pana"),
    PANEL_GROUP("PANEL_GROUP", "Pana Family"),
    TWO_D_PANEL("TWO_D_PANEL", "Two Digit Panel"),
    HARF("HARF", "Harf");

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    private final String id;
    private final String displayName;

    GameOption(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }


    public static GameType toGameType(GameOption gameOption) {
        if (GameOption.SP.equals(gameOption)) {
            return GameType.SP;
        } else if (GameOption.DP.equals(gameOption)) {
            return GameType.DP;
        } else if (GameOption.TP.equals(gameOption)) {
            return GameType.TP;
        } else if (GameOption.SINGLE.equals(gameOption)) {
            return GameType.SINGLE;
        } else if (GameOption.JODI.equals(gameOption)) {
            return GameType.JODI;
        } else if (GameOption.HALF_SANGAM.equals(gameOption)) {
            return GameType.HALF_SANGAM;
        } else if (GameOption.FULL_SANGAM.equals(gameOption)) {
            return GameType.FULL_SANGAM;
        } else if (GameOption.SP_MOTOR.equals(gameOption)) {
            return GameType.SP;
        } else if (GameOption.DP_MOTOR.equals(gameOption)) {
            return GameType.DP;
        } else if (GameOption.GROUP_JODI.equals(gameOption)) {
            return GameType.JODI;
        } else if (GameOption.ODD_EVEN.equals(gameOption)) {
            return GameType.SINGLE;
        } else if (GameOption.RED_BRACKET.equals(gameOption)) {
            return GameType.JODI;
        } else if (GameOption.PANEL_GROUP.equals(gameOption)) {
            return GameType.JODI;
        } else if (GameOption.TWO_D_PANEL.equals(gameOption)) {
            return GameType.JODI;
        } else if (GameOption.HARF.equals(gameOption)) {
            return GameType.HARF;
        }

        return null;
    }
}
