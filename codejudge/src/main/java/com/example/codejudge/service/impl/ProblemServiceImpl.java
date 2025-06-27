package com.example.codejudge.service.impl;

import com.example.codejudge.dao.ProblemDao;
import com.example.codejudge.model.Problem;
import com.example.codejudge.service.ProblemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {

    private final ProblemDao problemDao;

    public ProblemServiceImpl(ProblemDao problemDao) {
        this.problemDao = problemDao;
    }

    @Override
    public void addProblem(Problem problem) {
        problemDao.save(problem);
    }

    @Override
    public void updateProblem(Problem problem) {
        problemDao.update(problem);
    }

    @Override
    public void deleteProblem(int id) {
        problemDao.deleteById(id);
    }

    @Override
    public List<Problem> getAllProblems() {
        return problemDao.findAll();
    }

    @Override
    public Problem getProblemById(int id) {
        return problemDao.findById(id);
    }
}