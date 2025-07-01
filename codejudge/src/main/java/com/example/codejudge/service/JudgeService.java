package com.example.codejudge.service;

import com.example.codejudge.dao.SubmissionDao;
import com.example.codejudge.model.Submission;
import com.example.codejudge.model.Problem;
import com.example.codejudge.dao.ProblemDao;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
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

            if("ai".equalsIgnoreCase(sub.getJudgeType())){
                result=callWenxinAndJudge(sub,problem);
                submissionDao.updateStatus(sub.getId(), result);
                continue;
            }

            else if ("java".equalsIgnoreCase(sub.getLanguage())) {
                String fullCode = wrapJavaCode(codeToRun);
                result = runJavaAndCompare(fullCode, problem.getInput(), problem.getOutput());
            }
            else if ("cpp".equalsIgnoreCase(sub.getLanguage()) || "C++".equalsIgnoreCase(sub.getLanguage())) {
                result = runCppAndCompare(codeToRun, problem.getInput(), problem.getOutput());
            }
            else {
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
    private String callWenxinAndJudge(Submission sub, Problem problem) {
        try {
            // 1. 构造 prompt 内容
            String prompt = String.format(
                    "你是一个编程助教，请根据以下信息判断学生代码是否正确：\n\n" +
                            "【题目描述】\n%s\n\n" +
                            "【输入示例】\n%s\n\n" +
                            "【输出示例】\n%s\n\n" +
                            "【提交语言】\n%s\n\n" +
                            "【学生代码】\n%s\n\n" +
                            "请遵循以下规则判断代码质量：\n" +
                            "1. 如果代码中存在大量语法错误，或者根本不是代码，只是无意义的胡写等情况，请回答 CE。\n" +
                            "2. 如果代码能运行，但不能得到正确答案，请回答 WA。\n" +
                            "3. 如果代码大致能运行，存在少量语法错误但容易纠正，回答SE。\n" +
                            "4. 如果代码能正确完成题目要求，请回答 AC。\n\n" +
                            "你需要给出明确回答：AC、WA、SE 或 CE，并且给出解释。",
                    problem.getDescription(),
                    problem.getInput(),
                    problem.getOutput(),
                    sub.getLanguage(),
                    sub.getCode()
            );

            // 2. 构造请求 JSON
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("model", "ernie-3.5-8k");

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", prompt));
            jsonMap.put("messages", messages);

            ObjectMapper mapper = new ObjectMapper();
            String jsonPayload = mapper.writeValueAsString(jsonMap);

            // 3. 创建连接
            URL url = new URL("https://qianfan.baidubce.com/v2/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer bce-v3/ALTAK-zEZIJYfOjQupxrsxzuUHm/79c10447262bd200eb69b10eab3323b4fea0f790");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // 4. 发送请求体
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 5. 读取响应体
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            System.out.println("AI 返回原文：\n" + response.toString());

            // 6. 简单包含判断（也可以进一步解析 JSON）
            if (response.toString().contains("AC")) return "AC";
            else if (response.toString().contains("WA")) return "WA";
            else if (response.toString().contains("SE")) return "SE";
            else return "CE";

        } catch (Exception e) {
            e.printStackTrace();
            return "AI_ERR";
        }
    }
}