package ru.otus.andrk.service.info;

import ru.otus.andrk.dto.BackEndServerInfo;

import java.util.Map;

public interface BackEndInfoService {

    Map<String, BackEndServerInfo> getInfo();

}
