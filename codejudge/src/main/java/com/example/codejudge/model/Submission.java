package com.example.codejudge.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Submission {
    private int id;
    private String username;
    private int problemId;
    private String language;
    private String code;
    private String status;
    private String judgeType;
    private LocalDateTime submitTime;
}
