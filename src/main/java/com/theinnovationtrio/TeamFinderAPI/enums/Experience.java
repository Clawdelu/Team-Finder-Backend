package com.theinnovationtrio.TeamFinderAPI.enums;

import lombok.Getter;

@Getter
public enum Experience {
    SIX_MONTHS("0-6 months"),
    ONE_YEAR("6-12 months"),
    TWO_YEARS("1-2 years"),
    FOUR_YEARS("2-4 years"),
    SEVEN_YEARS("4-7 years"),
    MORE_THAN_7_YEARS(">7 years");
    private final String description;

    Experience(String description) {
        this.description = description;
    }

}
