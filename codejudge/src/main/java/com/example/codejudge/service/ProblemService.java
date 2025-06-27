package com.example.codejudge.service;

import com.example.codejudge.model.Problem;

import java.util.List;

public interface ProblemService {
    void addProblem(Problem problem);
    void updateProblem(Problem problem);
    void deleteProblem(int id);
    List<Problem> getAllProblems();
    Problem getProblemById(int id);
}