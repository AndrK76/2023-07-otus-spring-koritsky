package ru.otus.andrk.model;

import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString(of = {"device", "level", "description"})
public class DeviceTrouble {
    private final Device device;
    private final TroubleLevel level;
    private final Date date;
    private final String description;

    public DeviceTrouble(Device device, TroubleLevel level, String description) {
        this.device = device;
        this.level = level;
        this.date = new Date();
        this.description = description;
    }
}
