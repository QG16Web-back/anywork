package com.qg.anywork.dao;

import com.qg.anywork.model.po.TestPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author FunriLy
 * @date 2017/7/12
 * From small beginnings comes great things.
 */
@Mapper
@Repository
public interface TestPaperDao {

    /**
     * 插入一张试卷简介
     *
     * @param Testpaper
     * @return
     */
    int insertTestpaper(@Param("TestPaper") TestPaper Testpaper);

    int updateTestpaper(@Param("TestPaper") TestPaper Testpaper);

    int deleteTestpaper(@Param("TestpaperId") Integer TestpaperId);

    TestPaper selectOne(@Param("TestpaperId") Integer TestpaperId);

    /**
     * 根据组织查找试卷
     *
     * @param organizationId 组织ID
     * @param createTime     创建时间
     * @return 试卷
     */
    List<TestPaper> findTestPaperByOrganizationIdAndTime(@Param("organizationId") int organizationId, @Param("createTime") Timestamp createTime);
}
