package com.drivingexam.controller;

import com.drivingexam.common.Result;
import com.drivingexam.entity.Question;
import com.drivingexam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
@CrossOrigin
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/add")
    public Result<?> addQuestion(@RequestBody Question question) {
        try {
            questionService.addQuestion(question);
            return Result.success("添加成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> deleteQuestion(@PathVariable Integer id) {
        try {
            questionService.deleteQuestion(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<?> updateQuestion(@RequestBody Question question) {
        try {
            questionService.updateQuestion(question);
            return Result.success("更新成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/info/{id}")
    public Result<Question> getQuestionInfo(@PathVariable Integer id) {
        try {
            Question question = questionService.getQuestionById(id);
            return Result.success(question);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public Result<List<Question>> getQuestionList() {
        List<Question> questions = questionService.getAllQuestions();
        return Result.success(questions);
    }

    @GetMapping("/list/category/{categoryId}")
    public Result<List<Question>> getQuestionsByCategory(@PathVariable Integer categoryId) {
        List<Question> questions = questionService.getQuestionsByCategoryId(categoryId);
        return Result.success(questions);
    }

    @GetMapping("/list/type/{type}")
    public Result<List<Question>> getQuestionsByType(@PathVariable String type) {
        List<Question> questions = questionService.getQuestionsByType(type);
        return Result.success(questions);
    }

    @GetMapping("/list/difficulty/{difficulty}")
    public Result<List<Question>> getQuestionsByDifficulty(@PathVariable Integer difficulty) {
        List<Question> questions = questionService.getQuestionsByDifficulty(difficulty);
        return Result.success(questions);
    }

    @GetMapping("/list/chapter/{chapter}")
    public Result<List<Question>> getQuestionsByChapterPath(@PathVariable String chapter) {
        List<Question> questions = questionService.getQuestionsByChapter(chapter);
        return Result.success(questions);
    }

    @GetMapping("/list/chapter")
    public Result<List<Question>> getQuestionsByChapter(@RequestParam String chapter) {
        List<Question> questions = questionService.getQuestionsByChapter(chapter);
        return Result.success(questions);
    }

    @GetMapping("/random")
    public Result<List<Question>> getRandomQuestions(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer difficulty,
            @RequestParam(defaultValue = "20") Integer limit) {
        List<Question> questions = questionService.getRandomQuestions(categoryId, type, difficulty, limit);
        return Result.success(questions);
    }

    @GetMapping("/count")
    public Result<Integer> countQuestions(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer difficulty) {
        int count = questionService.countQuestions(categoryId, type, difficulty);
        return Result.success(count);
    }
}
