package ru.otus.andrk.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TroubleLevel {
    LOWER(1),

    MEDIUM(5),

    HIGH(10);

    private final int level;

    public static TroubleLevel troubleByLevel(int level) {
        if (level >= TroubleLevel.HIGH.getLevel()) {
            return TroubleLevel.HIGH;
        } else if (level >= TroubleLevel.MEDIUM.getLevel()) {
            return TroubleLevel.MEDIUM;
        } else {
            return TroubleLevel.LOWER;
        }
    }
}
