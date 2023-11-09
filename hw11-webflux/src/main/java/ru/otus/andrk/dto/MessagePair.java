package ru.otus.andrk.dto;

import java.io.Serializable;

public record MessagePair(String key, String message) implements Serializable {
}
