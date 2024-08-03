package com.theinnovationtrio.TeamFinderAPI.enums;

import lombok.Getter;

@Getter
public enum Level {
    LEARNS("1 - Learns"),
    KNOWS("2 - Knows"),
    DOES("3 - Does"),
    HELPS("4 - Helps"),
    TEACHES("5 - Teaches");

    private final String description;

    Level(String description) {
        this.description = description;
    }

}
