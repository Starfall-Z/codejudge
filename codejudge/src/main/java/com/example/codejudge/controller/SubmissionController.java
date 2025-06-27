package com.example.codejudge.controller;

import com.example.codejudge.dao.SubmissionDao;
import com.example.codejudge.model.Submission;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionDao submissionDao;

    public SubmissionController(SubmissionDao submissionDao) {
        this.submissionDao = submissionDao;
    }

    @PostMapping
    public String submit(@RequestBody Submission submission) {
        // 手动填充一些字段
        submission.setStatus("Pending");
        submission.setSubmitTime(LocalDateTime.now());

        submissionDao.save(submission);
        return "提交成功，等待评测结果";
    }

    @GetMapping("/{username}")
    public List<Submission> getByUser(@PathVariable String username) {
        return submissionDao.findAllByUsername(username);
    }

    @GetMapping("/all")
    public List<Submission> getAll() {
        return submissionDao.findAll(); // 👈 需要新增 Dao 方法
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        submissionDao.deleteById(id); // 👈 也需要新增 Dao 方法
        return "删除成功";
    }
}