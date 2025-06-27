package com.example.codejudge.dao;

import com.example.codejudge.model.Problem;
import java.util.List;

public interface ProblemDao {
    void save(Problem p);
    List<Problem> findAll();
    Problem findById(int id);

    void deleteById(int id);
    void update(Problem p);
    List<Problem> findPage(int offset, int size);
}
