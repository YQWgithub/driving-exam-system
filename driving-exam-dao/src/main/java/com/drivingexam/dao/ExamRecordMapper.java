package com.drivingexam.dao;

import com.drivingexam.entity.ExamRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExamRecordMapper {
    int insert(ExamRecord examRecord);

    int deleteById(Integer id);

    int update(ExamRecord examRecord);

    ExamRecord selectById(Integer id);

    List<ExamRecord> selectAll();

    List<ExamRecord> selectByUserId(Integer userId);

    List<ExamRecord> selectByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") Integer status);

    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    int updateScore(@Param("id") Integer id, @Param("score") Integer score, @Param("total") Integer total,
            @Param("isPass") Integer isPass);
}
