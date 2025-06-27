package com.example.codejudge.service;

import com.example.codejudge.dao.SubmissionDao;
import com.example.codejudge.model.Submission;
import com.example.codejudge.model.Problem;
import com.example.codejudge.dao.ProblemDao;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class JudgeService {

    private final SubmissionDao submissionDao;
    private final ProblemDao problemDao;

    public JudgeService(SubmissionDao submissionDao, ProblemDao problemDao) {
        this.submissionDao = submissionDao;
        this.problemDao = problemDao;
    }

    @Scheduled(fixedRate = 10000)
    public void judgePending() throws IOException {
        List<Submission> pendingList = submissionDao.findPending();

        for (Submission sub : pendingList) {
            Problem problem = problemDao.findById(sub.getProblemId());

            String codeToRun = sub.getCode();
            String result;

            if ("java".equalsIgnoreCase(sub.getLanguage())) {
                String fullCode = wrapJavaCode(codeToRun);
                result = runJavaAndCompare(fullCode, problem.getInput(), problem.getOutput());
            } else if ("cpp".equalsIgnoreCase(sub.getLanguage()) || "C++".equalsIgnoreCase(sub.getLanguage())) {
                result = runCppAndCompare(codeToRun, problem.getInput(), problem.getOutput());
            } else {
                result = "UNSUPPORTED";
            }

            submissionDao.updateStatus(sub.getId(), result);
        }
    }

    private String wrapJavaCode(String userCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("import java.util.*;\n");
        sb.append("public class Main {\n");
        sb.append("    public static void main(String[] args) {\n");
        sb.append("        Scanner scanner = new Scanner(System.in);\n");
        sb.append("        String input = scanner.nextLine();\n");
        sb.append("        System.out.println(new Solution().solve(input));\n");
        sb.append("    }\n");
        sb.append("}\n\n");

        sb.append("class Solution {\n");
        sb.append("    public String solve(String input) {\n");

        // 缩进用户代码
        for (String line : userCode.split("\n")) {
            sb.append("        ").append(line).append("\n");
        }

        sb.append("    }\n");
        sb.append("}\n");

        return sb.toString();
    }

    private String runJavaAndCompare(String userCode, String input, String expectedOutput) {
        try {
            // 包装用户代码为完整 Java 程序
            //String fullCode = wrapJavaCode(userCode);

            // 写入 Main.java
            File file = new File("Main.java");
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(userCode);
            }

            // 编译
            Process compile = Runtime.getRuntime().exec("javac Main.java");
            compile.waitFor();

            if (compile.exitValue() != 0) {
                return "CE";
            }

            // 执行
            Process exec = Runtime.getRuntime().exec("java Main");
            try (
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exec.getOutputStream()));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()))
            ) {
                writer.write(input);
                writer.flush();
                writer.close();

                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(" ");
                }

                return output.toString().trim().equals(expectedOutput.trim()) ? "AC" : "WA";
            }

        } catch (Exception e) {
            e.printStackTrace();  // 可打印日志
            return "RE";
        }
    }
    private String runCppAndCompare(String userCode, String input, String expectedOutput) {
        try {
            // 写入 Main.cpp
            File file = new File("Main.cpp");
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(userCode);
            }

            // 编译
            Process compile = Runtime.getRuntime().exec("g++ Main.cpp -o MainCpp.exe");
            compile.waitFor();

            if (compile.exitValue() != 0) {
                return "CE";
            }

            // 执行
            Process exec = Runtime.getRuntime().exec("MainCpp.exe");
            try (
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(exec.getOutputStream()));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()))
            ) {
                writer.write(input);
                writer.flush();
                writer.close();

                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(" ");
                }

                return output.toString().trim().equals(expectedOutput.trim()) ? "AC" : "WA";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "RE";
        }
    }
}