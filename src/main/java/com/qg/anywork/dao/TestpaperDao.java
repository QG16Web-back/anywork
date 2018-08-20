package com.qg.anywork.dao;

import com.qg.anywork.model.po.TestPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by FunriLy on 2017/7/12.
 * From small beginnings comes great things.
 */
@Mapper
@Repository
public interface TestpaperDao {

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
}
