package com.drivingexam.controller;

import com.drivingexam.common.Result;
import com.drivingexam.entity.WrongQuestion;
import com.drivingexam.service.WrongQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wrong")
@CrossOrigin
public class WrongController {

    @Autowired
    private WrongQuestionService wrongQuestionService;

    /**
     * 添加错题
     */
    @PostMapping("/add")
    public Result<?> addWrongQuestion(@RequestBody Map<String, Object> params) {
        Integer userId = params.get("userId") != null ? ((Number) params.get("userId")).intValue() : 1;
        Integer questionId = params.get("questionId") != null ? ((Number) params.get("questionId")).intValue() : 0;

        if (questionId == 0) {
            return Result.error("题目ID不能为空");
        }

        WrongQuestion wrongQuestion = new WrongQuestion();
        wrongQuestion.setUserId(userId);
        wrongQuestion.setQuestionId(questionId);
        wrongQuestion.setWrongAnswer((String) params.get("wrongAnswer"));
        wrongQuestion.setCorrectAnswer((String) params.get("correctAnswer"));
        wrongQuestion.setCreateTime(new Date());

        int result = wrongQuestionService.saveOrUpdateWrongQuestion(wrongQuestion);
        if (result > 0) {
            return Result.success("添加成功", null);
        }
        return Result.success("错题已存在", null);
    }

    /**
     * 获取错题列表（带题目详情）
     */
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> getWrongQuestions(@RequestParam Integer userId) {
        List<Map<String, Object>> wrongQuestions = wrongQuestionService.getWrongQuestionDetails(userId);
        return Result.success(wrongQuestions);
    }

    /**
     * 按ID删除错题
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteWrongQuestionById(@PathVariable("id") Integer id) {
        int result = wrongQuestionService.deleteWrongQuestion(id);
        if (result > 0) {
            return Result.success("删除成功", null);
        }
        return Result.error("删除失败");
    }

    /**
     * 按用户ID和题目ID删除错题
     */
    @DeleteMapping("/remove")
    public Result<?> deleteWrongQuestion(@RequestParam Integer userId, @RequestParam Integer questionId) {
        int result = wrongQuestionService.removeWrongQuestion(userId, questionId);
        if (result > 0) {
            return Result.success("删除成功", null);
        }
        return Result.error("删除失败");
    }

    /**
     * 清空用户所有错题
     */
    @DeleteMapping("/clear")
    public Result<?> clearWrongQuestions(@RequestParam Integer userId) {
        int result = wrongQuestionService.clearWrongQuestions(userId);
        return Result.success("清空成功，共删除 " + result + " 条错题", null);
    }

    /**
     * 兼容旧接口：按路径参数获取错题列表
     */
    @GetMapping("/list/{userId}")
    public Result<List<Map<String, Object>>> getWrongQuestionsByPath(@PathVariable Integer userId) {
        List<Map<String, Object>> wrongQuestions = wrongQuestionService.getWrongQuestionDetails(userId);
        return Result.success(wrongQuestions);
    }

    /**
     * 兼容旧接口：按路径参数删除错题
     */
    @DeleteMapping("/remove/{userId}/{questionId}")
    public Result<?> removeWrongQuestionByPath(@PathVariable Integer userId, @PathVariable Integer questionId) {
        int result = wrongQuestionService.removeWrongQuestion(userId, questionId);
        if (result > 0) {
            return Result.success("删除成功", null);
        }
        return Result.error("删除失败");
    }
}
