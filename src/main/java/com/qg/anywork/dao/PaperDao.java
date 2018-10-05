package com.qg.anywork.dao;

import com.qg.anywork.model.po.TestPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create by ming on 18-10-5 下午11:24
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Mapper
@Repository
public interface PaperDao {

    /**
     * 添加试卷记录
     *
     * @param testPaper 试卷实体
     * @return int
     */
    int insertTestPaper(@Param("testPaper") TestPaper testPaper);


    /**
     * 插入组织与试卷的关系记录
     *
     * @param testPaperId     试卷ID
     * @param organizationIds 组织ID集合
     * @return int
     */
    int insertTestPaperOrganization(@Param("testPaperId") Integer testPaperId, @Param("organizationIds") List<Integer> organizationIds);

    /**
     * 更新试卷的分数
     *
     * @param testPaperId 试卷ID
     * @param score       试卷分数
     * @return int
     */
    int updateTestPaperScore(@Param("testPaperId") Integer testPaperId, @Param("score") Integer score);

    /**
     * 修改试卷信息
     *
     * @param testPaper 试卷实体
     * @return int
     */
    int updateTestPaperInfo(@Param("testPaper") TestPaper testPaper);

    /**
     * 根据ID查找试卷
     *
     * @param testPaperId 试卷ID
     * @return 试卷
     */
    TestPaper findByTestPaperId(@Param("testPaperId") Integer testPaperId);
}
