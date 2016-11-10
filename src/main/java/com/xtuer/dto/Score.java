package com.xtuer.dto;

import lombok.Getter;
import lombok.Setter;

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
    private String name;
    private String scoreCertNo;

}
