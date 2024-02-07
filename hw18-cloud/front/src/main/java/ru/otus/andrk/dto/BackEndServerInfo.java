package ru.otus.andrk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackEndServerInfo {

    private String host;

    private int port;

    @JsonProperty("db-type")
    private String dbType;
}
