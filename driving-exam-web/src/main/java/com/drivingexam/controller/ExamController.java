package com.drivingexam.controller;

import com.drivingexam.common.Result;
import com.drivingexam.entity.AnswerDetail;
import com.drivingexam.entity.ExamRecord;
import com.drivingexam.entity.Question;
import com.drivingexam.entity.WrongQuestion;
import com.drivingexam.service.ExamRecordService;
import com.drivingexam.service.QuestionService;
import com.drivingexam.service.WrongQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/exam")
@CrossOrigin
public class ExamController {

    @Autowired
    private ExamRecordService examRecordService;

    @Autowired
    private QuestionService questionService;

    @Autowired(required = false)
    private WrongQuestionService wrongQuestionService;

    /**
     * 开始考试
     * 1. 从数据库随机抽取题目
     * 2. 创建考试记录并插入数据库
     * 3. 返回真实的考试记录ID和题目列表
     */
    @PostMapping("/start")
    public Result<Map<String, Object>> startExam(@RequestBody Map<String, Object> params) {
        Integer userId = params.get("userId") != null ? ((Number) params.get("userId")).intValue() : 1;
        Integer categoryId = params.get("categoryId") != null ? ((Number) params.get("categoryId")).intValue() : null;
        Integer totalQuestions = params.get("totalQuestions") != null
                ? ((Number) params.get("totalQuestions")).intValue() : 20;

        List<Question> questions = questionService.getRandomQuestions(categoryId, null, null, totalQuestions);
        if (questions == null || questions.isEmpty()) {
            return Result.error("题库中暂无题目，请联系管理员添加题目");
        }

        ExamRecord examRecord = new ExamRecord();
        examRecord.setUserId(userId);
        examRecord.setPaperId(1);
        examRecord.setStartTime(new Date());
        examRecord.setScore(0);
        examRecord.setTotal(100);
        examRecord.setIsPass(0);
        examRecord.setStatus(0);
        examRecord.setCreateTime(new Date());

        examRecordService.createExamRecord(examRecord);

        Map<String, Object> data = new HashMap<>();
        data.put("examRecordId", examRecord.getId());
        data.put("questions", questions);

        return Result.success("考试开始成功", data);
    }

    /**
     * 提交试卷（带考试记录ID）
     * 1. 根据考试记录ID查询考试记录
     * 2. 逐题判分并保存答题详情
     * 3. 错题记录到错题本
     * 4. 更新考试记录的成绩和状态
     */
    @PostMapping("/submit/{examRecordId}")
    public Result<Map<String, Object>> submitExamWithRecordId(
            @PathVariable Integer examRecordId,
            @RequestBody Map<String, Object> params) {
        try {
            Integer userId = params.get("userId") != null ? ((Number) params.get("userId")).intValue() : 1;
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> answers = (List<Map<String, Object>>) params.get("answers");

            Map<String, Object> result = examRecordService.submitExamWithAnswers(examRecordId, userId, answers);
            return Result.success("提交成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 保存单题答案（边答题边保存）
     */
    @PostMapping("/answer")
    public Result<?> saveAnswer(@RequestBody AnswerDetail answerDetail) {
        try {
            Question question = questionService.getQuestionById(answerDetail.getQuestionId());
            if (question == null) {
                return Result.error("题目不存在");
            }

            boolean isCorrect = answerDetail.getUserAnswer().equals(question.getAnswer());
            answerDetail.setIsCorrect(isCorrect ? 1 : 0);

            examRecordService.saveAnswer(answerDetail);

            return Result.success("答案保存成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询考试记录详情
     */
    @GetMapping("/record/{id}")
    public Result<ExamRecord> getExamRecord(@PathVariable Integer id) {
        ExamRecord examRecord = examRecordService.getExamRecordById(id);
        if (examRecord == null) {
            return Result.error("考试记录不存在");
        }
        return Result.success(examRecord);
    }

    /**
     * 查询用户的考试记录列表
     */
    @GetMapping("/list")
    public Result<List<ExamRecord>> getExamRecordsByUserId(@RequestParam Integer userId) {
        List<ExamRecord> examRecords = examRecordService.getExamRecordsByUserId(userId);
        return Result.success(examRecords != null ? examRecords : new ArrayList<>());
    }

    @GetMapping("/all")
    public Result<List<ExamRecord>> getAllExamRecords() {
        List<ExamRecord> examRecords = examRecordService.getAllExamRecords();
        return Result.success(examRecords != null ? examRecords : new ArrayList<>());
    }

    /**
     * 按状态查询考试记录
     */
    @GetMapping("/record/user/{userId}/status/{status}")
    public Result<List<ExamRecord>> getExamRecordsByUserIdAndStatus(
            @PathVariable Integer userId, @PathVariable Integer status) {
        List<ExamRecord> examRecords = examRecordService.getExamRecordsByUserIdAndStatus(userId, status);
        return Result.success(examRecords != null ? examRecords : new ArrayList<>());
    }

    /**
     * 查询答题详情
     */
    @GetMapping("/answer/{examRecordId}")
    public Result<List<AnswerDetail>> getAnswerDetails(@PathVariable Integer examRecordId) {
        List<AnswerDetail> answerDetails = examRecordService.getAnswerDetailsByExamRecordId(examRecordId);
        return Result.success(answerDetails != null ? answerDetails : new ArrayList<>());
    }

    /**
     * 计算考试分数
     */
    @GetMapping("/score/{examRecordId}")
    public Result<Integer> calculateScore(@PathVariable Integer examRecordId) {
        int score = examRecordService.calculateScore(examRecordId);
        return Result.success(score);
    }

    /**
     * 获取随机题目（用于模拟考试）
     */
    @GetMapping("/random")
    public Result<List<Question>> getRandomQuestions(@RequestParam(defaultValue = "20") Integer limit) {
        List<Question> questions = questionService.getRandomQuestions(null, null, null, limit);
        return Result.success(questions != null ? questions : new ArrayList<>());
    }

    /**
     * 提交试卷（简化版，不传examRecordId）
     * 会自动创建考试记录
     */
    @PostMapping("/submit")
    public Result<Map<String, Object>> submitExamSimple(@RequestBody Map<String, Object> params) {
        Integer userId = params.get("userId") != null ? ((Number) params.get("userId")).intValue() : 1;
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> answers = (List<Map<String, Object>>) params.get("answers");

        if (answers == null || answers.isEmpty()) {
            return Result.error("答案不能为空");
        }

        ExamRecord examRecord = new ExamRecord();
        examRecord.setUserId(userId);
        examRecord.setPaperId(1);
        examRecord.setStartTime(new Date());
        examRecord.setScore(0);
        examRecord.setTotal(100);
        examRecord.setIsPass(0);
        examRecord.setStatus(0);
        examRecord.setCreateTime(new Date());

        examRecordService.createExamRecord(examRecord);

        try {
            Map<String, Object> result = examRecordService.submitExamWithAnswers(examRecord.getId(), userId, answers);
            return Result.success("提交成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
