package com.xtuer.mapper;

import com.xtuer.dto.Limitation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * limit 限制库
 *
 * Created by microacup on 2016/10/24.
 */
public interface LimitationMapper {
    List<Limitation> findByIdnoAndCertno(@Param("idno") String idno, @Param("certno") String certno);
}
