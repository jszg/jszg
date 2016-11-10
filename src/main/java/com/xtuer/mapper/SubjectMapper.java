package com.xtuer.mapper;

import com.xtuer.dto.Subject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任教学科
 *
 * Created by microacup on 2016/10/17.
 */
public interface SubjectMapper {
    List<Subject> findRoots(@Param("province") int province, @Param("certTypeId") int certTypeId);
    List<Subject> findByParentAndProvince(@Param("parent") int parent, @Param("province") int province);
    List<Subject> findByCertType(@Param("certTypeId") int certTypeId);
    List<Subject> findByParent(@Param("parent")int parent);

    // 现任教学科父节点
    List<Subject> findBySubjectTypeAndProvince(@Param("teachGrade") int teachGrade, @Param("province") int province);
    // 现任教学科子节点 @see findByParentAndProvince

    //注册第四步现任教学科按名称搜索
    List<Subject> findByName(@Param("teachGradeId") int teachGradeId, @Param("provinceId") int provinceId, @Param("name") String name);

    //非统考第三步加载任教学科树父节点
    List<Subject> findByCertTypeAndProvince(@Param("provinceId") int provinceId,  @Param("teachGrade") int teachGrade);

    //非统考第三步加载任教学科树子节点
    List<Subject> findChildByCertTypeAndProvince(@Param("provinceId") int provinceId,  @Param("parentId") int parentId);

    //非统考第三步加载任教学科按名称查询
    List<Subject> findRequestSubjectByName(@Param("teachGrade") int teachGrade, @Param("provinceId") int provinceId, @Param("name") String name);


}
