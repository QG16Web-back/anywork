package com.qg.anywork.service.impl;

import com.qg.anywork.dao.ChapterDao;
import com.qg.anywork.dao.OrganizationDao;
import com.qg.anywork.enums.StatEnum;
import com.qg.anywork.exception.organization.OrganizationException;
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
            throw new OrganizationException(StatEnum.ORGANIZATION_NOT_EXIST);
        }
        return new RequestResult<>(1, "获取成功", chapterDao.getByOrganizationId(organizationId));
    }

    @Override
    public RequestResult addChapter(Chapter chapter) {
        if (organizationDao.getById(chapter.getOrganizationId()) == null) {
            throw new OrganizationException(StatEnum.ORGANIZATION_NOT_EXIST);
        }
        if (chapter.getChapterName().length() > 10) {
            throw new OrganizationException(StatEnum.CHAPTER_TOO_LONG);
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
