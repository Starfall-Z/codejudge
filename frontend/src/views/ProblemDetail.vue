<template>

  <div class="page-bg">
    <div class="problem-detail">
      <h2>{{ problem.title }}</h2>
      <div class="meta">
        时间限制：{{ problem.timeLimit }} ms｜内存限制：{{ problem.memoryLimit }} MB
      </div>
      <div class="description">
        <h3>题目描述</h3>
        <p>{{ problem.description }}</p >
        <h3>输入</h3>
        <pre>{{ problem.input }}</pre>
        <h3>输出</h3>
        <pre>{{ problem.output }}</pre>
      </div>  <h3>代码提交</h3>
  <h4>选择语言</h4>
  <select v-model="language" class="custom-select">
    <option value="java">Java</option>
    <option value="cpp">C++</option>
  </select>

  <textarea v-model="code" placeholder="在此处输入你的代码" rows="12"></textarea>

  <!-- ✅ 按钮横向排布 -->
  <div class="button-group">
    <button class="back-btn" @click="goBack">返回 🏃‍</button>
    <button class="submit-btn" @click="submit">提交 🚀</button>
  </div>
</div>

  </div>
</template>  <script>
export default {
  name: "ProblemDetail",
  data() {
    return {
      problem: {},
      code: "",
      language:"cpp"
    };
  },
  mounted() {
    const id = this.$route.params.id;
    fetch(`/api/problems/${id}`)
      .then(res => res.json())
      .then(data => {
        this.problem = data.data;
      });
  },
  methods: {
    submit() {
      const id = this.$route.params.id;
      fetch("/api/submissions", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          problemId: id,
          code: this.code,
          username: localStorage.getItem("username") || "匿名用户",
          language: this.language
        })
      }).then(() => {
        alert("提交成功！");
        this.code = "";
      });
    },
    goBack(){
    this.$router.push('/problems');
    }
  }
};
</script>  <style scoped>
.problem-detail {
  padding: 40px;
  max-width: 800px;
  margin: auto;
}

.meta {
  color: #666;
  font-size: 14px;
  margin-bottom: 12px;
}

.description {
  margin-bottom: 24px;
}

.page-bg {
  min-height: 100vh;
  background: linear-gradient(to right, #74ebd5, #acb6e5);
  padding: 40px 0;
}

.problem-detail {
  padding: 40px;
  max-width: 800px;
  margin: auto;
  background: rgba(255, 255, 255, 0.33); /* 半透明白 */
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.25);
  backdrop-filter: blur(16px); /* 毛玻璃核心 */
  -webkit-backdrop-filter: blur(16px); /* Safari 兼容 */
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.18);
}

.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.back-btn {
  padding: 10px 20px;
  background-color: #f28500;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
}

.back-btn:hover {
  background-color: #d96c00;
}

.submit-btn {
  padding: 10px 20px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
}

.submit-btn:hover {
  background-color: #337ecc;
}

.custom-select {
  width: 12%;
  padding: 10px 12px;
  padding-right: 36px; /* 给箭头留空间 */
  margin-bottom: 16px;
  border: 1px solid #ccc;
  border-radius: 10px;
  font-size: 14px;
  background-color: #fff;
  color: #333;
  appearance: none;
  -webkit-appearance: none;
  background-image: linear-gradient(45deg, transparent 50%, #999 50%),
                    linear-gradient(-45deg, transparent 50%, #999 50%);
  background-position: right 16px center, right 10px center;
  background-size: 7px 7px;
  background-repeat: no-repeat;
  transition: border-color 0.3s, box-shadow 0.3s;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.05);
}

.custom-select:focus {
  border-color: #409eff;
  outline: none;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

pre {
  background: #f4f4f4;
  padding: 12px;
  border-radius: 8px;
  white-space: pre-wrap;
}

textarea {
  width: 97%;
  padding: 12px;
  font-family: monospace;
  font-size: 14px;
  margin-top: 12px;
  border-radius: 8px;
  border: 1px solid #ccc;
}

button {
  margin-top: 12px;
  padding: 10px 20px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

button:hover {
  background-color: #337ecc;
}

</style>