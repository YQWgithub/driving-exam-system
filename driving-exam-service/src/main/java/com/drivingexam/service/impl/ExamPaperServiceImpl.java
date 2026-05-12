package com.drivingexam.service.impl;

import com.drivingexam.dao.ExamPaperMapper;
import com.drivingexam.entity.ExamPaper;
import com.drivingexam.service.ExamPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExamPaperServiceImpl implements ExamPaperService {

    @Autowired
    private ExamPaperMapper examPaperMapper;

    @Override
    public int addExamPaper(ExamPaper examPaper) {
        return examPaperMapper.insert(examPaper);
    }

    @Override
    public int deleteExamPaper(Integer id) {
        return examPaperMapper.deleteById(id);
    }

    @Override
    public int updateExamPaper(ExamPaper examPaper) {
        return examPaperMapper.update(examPaper);
    }

    @Override
    public ExamPaper getExamPaperById(Integer id) {
        return examPaperMapper.selectById(id);
    }

    @Override
    public List<ExamPaper> getAllExamPapers() {
        return examPaperMapper.selectAll();
    }
}
