package com.github.dactiv.framework.spring.security.test.controller;

import com.github.dactiv.framework.commons.RestResult;
import com.github.dactiv.framework.security.enumerate.ResourceType;
import com.github.dactiv.framework.security.plugin.Plugin;
import com.github.dactiv.framework.spring.security.test.entity.OperationDataEntity;
import com.github.dactiv.framework.spring.security.test.service.OperationDataService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Plugin(
        name = "OperateDataController",
        id = "operateData",
        type = ResourceType.Menu,
        sources = "test"
)
@RequestMapping("operateData")
public class OperationDataController {

    private final OperationDataService operationDataService;

    public OperationDataController(OperationDataService operationDataService) {
        this.operationDataService = operationDataService;
    }

    @PostMapping("save")
    @Plugin(name = "save", audit = true, operationDataTrace = true)
    public RestResult<Integer> save(@RequestBody OperationDataEntity operationDataEntity) {
        operationDataService.save(operationDataEntity);
        return RestResult.ofSuccess(operationDataEntity.getId());
    }

    @PostMapping("delete")
    @Plugin(name = "delete", audit = true, operationDataTrace = true)
    public RestResult<?> delete(@RequestParam List<Integer> ids) {
        return RestResult.of("删除 " + ids.size() + " 记录成功");
    }

    @GetMapping("query")
    public List<OperationDataEntity> query() {
        return operationDataService.lambdaQuery().eq(OperationDataEntity::getName, "test").list();
    }
}
