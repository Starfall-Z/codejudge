package com.example.codejudge.controller;

import com.example.codejudge.model.Problem;
import com.example.codejudge.service.ProblemService;
import com.example.codejudge.util.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    // 添加题目
    @PostMapping
    public Result<String> addProblem(@RequestBody Problem problem) {
        problemService.addProblem(problem);
        return Result.success("题目添加成功");
    }

    // 修改题目
    @PutMapping("/{id}")
    public Result<String> updateProblem(@PathVariable int id, @RequestBody Problem problem) {
        problem.setId(id);
        problemService.updateProblem(problem);
        return Result.success("题目修改成功");
    }

    // 删除题目
    @DeleteMapping("/{id}")
    public Result<String> deleteProblem(@PathVariable int id) {
        problemService.deleteProblem(id);
        return Result.success("题目删除成功");
    }

    // 获取所有题目
    @GetMapping
    public Result<List<Problem>> listAll() {
        List<Problem> problems = problemService.getAllProblems();
        return Result.success(problems);
    }

    // 根据ID获取题目
    @GetMapping("/{id}")
    public Result<Problem> getById(@PathVariable int id) {
        Problem problem = problemService.getProblemById(id);
        if (problem == null) {
            return Result.error("题目未找到");
        }
        return Result.success(problem);
    }
}