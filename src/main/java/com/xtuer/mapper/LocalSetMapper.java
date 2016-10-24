package com.xtuer.mapper;

import com.xtuer.dto.LocalSet;
import com.xtuer.dto.LocalSetInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 确认点
 *
 * Created by microacup on 2016/10/24.
 */
public interface LocalSetMapper {
    List<LocalSet> findByOrgId(@Param("orgId") int orgId);
    LocalSetInfo findById(@Param("id") int id);
}
