package com.xtuer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 任教学科 dto.Subject:
 * subject_parentId: [{id, name}, {}]
 *
 * Created by microacup on 2016/10/17.
 */
@Getter
@Setter
public class Score {
    private int id;
    private String  name;
    private Integer idType;
    private String  idTypeName;
    private String  idNo;
    private String  scoreCertNo;
    private Date    expiredTime;//截止日期
    private Integer certType;
    private String  certTypeName;
    private Integer subject;
    private String  subjectName;
    private Integer adminLevel;












}
