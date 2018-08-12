package com.qg.anywork.service.impl;

import com.qg.anywork.dao.ChapterDao;
import com.qg.anywork.dao.OrganizationDao;
import com.qg.anywork.exception.OrganizationException;
import com.qg.anywork.model.dto.RequestResult;
import com.qg.anywork.model.po.Chapter;
import com.qg.anywork.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create by ming on 18-8-5 下午10:15
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Service
public class ChapterServiceImpl implements ChapterService {

    @Autowired
    private ChapterDao chapterDao;
    @Autowired
    private OrganizationDao organizationDao;

    @Override
    public RequestResult<List<Chapter>> getByOrganizationId(int organizationId) {
        if (organizationDao.getById(organizationId) == null) {
            throw new OrganizationException("组织id不存在");
        }
        return new RequestResult<>(1, "获取成功", chapterDao.getByOrganizationId(organizationId));
    }

    @Override
    public RequestResult addChapter(Chapter chapter) {
        if (organizationDao.getById(chapter.getOrganizationId()) == null) {
            throw new OrganizationException("组织id不存在");
        }
        if (chapter.getChapterName().length() > 10) {
            throw new OrganizationException("章节名过长");
        }
        chapterDao.addChapter(chapter);
        return new RequestResult<>(1, "添加成功", chapter);
    }

    @Override
    public RequestResult deleteChapter(int chapterId) {
        chapterDao.deleteChapter(chapterId);
        return new RequestResult(1, "删除成功");
    }

}
