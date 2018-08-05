package com.qg.anywork.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author logan
 * @date 2017/7/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {

    /**
     * 章节ID
     */
    private int chapterId;


    /**
     * 组织ID
     */
    private int organizationId;

    /**
     * 章节名称
     */
    private String chapterName;
}
