// package: com.example.codejudge.service

package com.example.codejudge.service;

import com.example.codejudge.model.Submission;

import java.util.List;

public interface SubmissionService {

    void submit(Submission submission);

    List<Submission> getByUsername(String username);

    List<Submission> getPending();

    void updateStatus(int id, String status);

    void deleteById(int id); // 🆕扩展的删除接口（为后续管理界面准备）
}