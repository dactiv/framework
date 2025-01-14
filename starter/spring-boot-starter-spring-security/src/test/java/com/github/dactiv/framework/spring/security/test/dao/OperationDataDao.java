package com.github.dactiv.framework.spring.security.test.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.dactiv.framework.spring.security.test.entity.OperationDataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OperationDataDao extends BaseMapper<OperationDataEntity> {
}
