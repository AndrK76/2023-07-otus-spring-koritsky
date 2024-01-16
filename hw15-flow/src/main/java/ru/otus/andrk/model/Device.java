package ru.otus.andrk.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString(of = {"name"})
public class Device {
    private final UUID id;

    private final String name;
}
