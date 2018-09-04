package com.qg.anywork.dao;

import com.qg.anywork.model.po.CollectionQuestion;
import com.qg.anywork.model.po.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by FunriLy on 2017/7/12.
 * From small beginnings comes great things.
 */
@Mapper
@Repository
public interface QuestionDao {

    /**
     * 插入题目
     *
     * @param question
     * @return
     */
    int insertQuestion(@Param("question") Question question);

    /**
     * 批量插入问题
     *
     * @param questlist
     * @return
     */
    int insertAllQuestion(List<Question> questlist);

    /**
     * 根据试卷id删除所有的题目
     *
     * @param testpaperId
     * @return
     */
    int deleteQuestion(@Param("testpaperId") int testpaperId);

    /**
     * 收藏题目
     *
     * @param collectionQuestion collectionQuestion
     * @return int
     */
    int collectQuestion(@Param("collectionQuestion") CollectionQuestion collectionQuestion);

    /**
     * 删除已收藏的题目
     *
     * @param studentId  学生ID
     * @param questionId 问题ID
     * @return int
     */
    int deleteCollection(@Param("studentId") Integer studentId, @Param("questionId") Integer questionId);

    /**
     * 根据id获取题目
     *
     * @param questionId id
     * @return 题目
     */
    String findContentById(@Param("questionId") Integer questionId);

    /**
     * 根据学生ID获取收藏题目id列表
     *
     * @param studentId 学生ID
     * @return id列表
     */
    List<Integer> findQuestionListByStudentId(@Param("studentId") Integer studentId);
}
