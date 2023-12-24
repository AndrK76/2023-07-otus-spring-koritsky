package ru.otus.andrk.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.stereotype.Component;
import ru.otus.andrk.service.ClearDataService;
import ru.otus.andrk.service.IdMappingService;
import ru.otus.andrk.service.TempColumnManageService;

@Component
@RequiredArgsConstructor
public class JobTasklet {
    private final TempColumnManageService tempColumnManageService;

    private final IdMappingService idMappingService;

    private final ClearDataService clearDataService;

    public MethodInvokingTaskletAdapter getCreateMongoIdsTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(tempColumnManageService);
        adapter.setTargetMethod("addMongoIds");
        return adapter;
    }

    public MethodInvokingTaskletAdapter getDropMongoIdsTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(tempColumnManageService);
        adapter.setTargetMethod("dropMongoIds");
        return adapter;
    }

    public MethodInvokingTaskletAdapter getFillIdTasklet(String entityName) {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(idMappingService);
        adapter.setTargetMethod("fillMap");
        adapter.setArguments(new Object[]{entityName});
        return adapter;
    }

    public MethodInvokingTaskletAdapter getClearMapsTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(idMappingService);
        adapter.setTargetMethod("clearMaps");
        return adapter;
    }

    public MethodInvokingTaskletAdapter getClearDataTasklet(boolean clear) {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(clearDataService);
        adapter.setTargetMethod("clearData");
        adapter.setArguments(new Object[]{clear});
        return adapter;
    }
}
