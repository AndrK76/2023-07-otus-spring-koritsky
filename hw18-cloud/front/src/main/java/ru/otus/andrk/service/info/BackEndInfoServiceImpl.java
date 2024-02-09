package ru.otus.andrk.service.info;

import com.netflix.discovery.EurekaClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.otus.andrk.config.BackendConfig;
import ru.otus.andrk.dto.BackEndServerInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class BackEndInfoServiceImpl implements BackEndInfoService {

    private final EurekaClient client;

    private final BackendConfig config;

    @Override
    public Map<String, BackEndServerInfo> getInfo() {
        var app = client.getApplication(config.getAppName());

        if (app == null) {
            return Collections.emptyMap();
        }
        Map<String, BackEndServerInfo> ret = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        for (var inst : app.getInstances()) {
            String infoUri = String.format("%s/actuator/info", inst.getHomePageUrl());
            var info = restTemplate.getForEntity(infoUri, HashMap.class).getBody();
            var instInf = new BackEndServerInfo(inst.getHostName(), inst.getPort(),
                    info.containsKey("db-type") ? (String) info.get("db-type") : null);
            ret.put(inst.getInstanceId(), instInf);
        }
        return ret;
    }
}
