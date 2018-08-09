package com.qg.anywork.service;

import com.qg.anywork.dto.RequestResult;
import com.qg.anywork.model.Chapter;

import java.util.List;

/**
 * Created by logan on 2017/7/11.
 */
public interface ChapterService {

    /***
     * 获取章节列表
     * @param organizationId
     * @return
     */
    RequestResult<List<Chapter>> getByOrganizationId(int organizationId);

    /***
     * 添加章节
     * @param chapter
     * @return
     */
    RequestResult addChapter(Chapter chapter);

    /***
     * 删除章节
     * @param chapterId
     * @return
     */
    RequestResult deleteChapter(int chapterId);
}
