package com.github.dactiv.framework.spring.security.test.service;

import com.github.dactiv.framework.mybatis.plus.service.BasicService;
import com.github.dactiv.framework.spring.security.test.dao.OperationDataDao;
import com.github.dactiv.framework.spring.security.test.entity.OperationDataEntity;
import org.springframework.stereotype.Service;

@Service
public class OperationDataService extends BasicService<OperationDataDao, OperationDataEntity> {
}
