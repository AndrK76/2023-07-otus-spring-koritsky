package ru.otus.andrk.controller.func;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.andrk.dto.BackEndServerInfo;
import ru.otus.andrk.service.info.BackEndInfoService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class BackendInfoController {
    private final BackEndInfoService infoService;

    @GetMapping("/api/v1/backend/info")
    public Map<String, BackEndServerInfo> getInfo() {
        return infoService.getInfo();
    }
}
