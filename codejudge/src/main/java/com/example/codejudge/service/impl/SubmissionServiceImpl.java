// package: com.example.codejudge.service.impl

package com.example.codejudge.service.impl;

import com.example.codejudge.dao.SubmissionDao;
import com.example.codejudge.model.Submission;
import com.example.codejudge.service.SubmissionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionDao submissionDao;

    public SubmissionServiceImpl(SubmissionDao submissionDao) {
        this.submissionDao = submissionDao;
    }

    @Override
    public void submit(Submission submission) {
        submission.setStatus("Pending");
        submission.setSubmitTime(LocalDateTime.now());
        submissionDao.save(submission);
    }

    @Override
    public List<Submission> getByUsername(String username) {
        return submissionDao.findAllByUsername(username);
    }

    @Override
    public List<Submission> getPending() {
        return submissionDao.findPending();
    }

    @Override
    public void updateStatus(int id, String status) {
        submissionDao.updateStatus(id, status);
    }

    @Override
    public void deleteById(int id) {
        submissionDao.deleteById(id); // ✅ 使用 Dao 层提供的接口
    }
}
