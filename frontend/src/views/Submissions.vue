<template>
  <div class="submission-page">
    <div class="submission-container">
      <div class="header">
        <h2>提交记录📖</h2>
        <button class="back-btn" @click="goBack">返回🏃‍</button>
      </div>

      <div v-if="loading" class="status">正在加载记录...</div>
      <div v-else-if="submissions.length === 0" class="status">暂无提交记录</div>
      <table v-else class="submission-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>题目编号</th>
            <th>用户名</th>
            <th>语言</th>
            <th>判题方式</th> <!-- ✅ 新增列 -->
            <th>状态</th>
            <th>提交时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in submissions" :key="s.id">
            <td>{{ s.id }}</td>
            <td>{{ s.problemId }}</td>
            <td>{{ s.username }}</td>
            <td>{{ s.language || '未知' }}</td>
            <td>{{ s.judgeType === 'ai' ? 'AI' : 'Local' }}</td> <!-- ✅ 新增字段显示 -->
            <td class="status-text" :data-status="s.status.toUpperCase()">{{ s.status }}</td>
            <td>{{ formatTime(s.submitTime) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: "Submissions",
  data() {
    return {
      submissions: [],
      loading: true
    };
  },
  mounted() {
    const username = localStorage.getItem("username");
    console.log("准备请求提交记录：", username);
    fetch(`/api/submissions/${username}`)
      .then(res => res.json())
      .then(data => {
        console.log("接口返回：", data);
        this.submissions = data;
        this.loading = false;
      })
      .catch(err => {
        console.error("加载失败：", err);
        this.loading = false;
      });
  },
  methods: {
    goBack() {
      this.$router.push('/dashboard');
    },
    formatTime(timeStr) {
      return new Date(timeStr).toLocaleString();
    }
  }
};
</script>

<style scoped>
.submission-page {
  min-height: 100vh;
  background: linear-gradient(to right, #74ebd5, #acb6e5);
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 60px 20px;
}

.submission-container {
  background: rgba(255, 255, 255, 0.33); /* 毛玻璃白色背景 */
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 40px;
  max-width: 1000px;
  width: 100%;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.4);
  color: #000;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.back-btn {
  background-color: #f28500;
  color: white;
  padding: 10px 16px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
}

.back-btn:hover {
  background-color: #d96c00;
}

.status {
  text-align: center;
  font-size: 16px;
  color: #666;
  margin-top: 30px;
}

.submission-table {
  width: 100%;
  border-collapse: collapse;
}

.submission-table th,
.submission-table td {
  padding: 12px 16px;
  border-bottom: 1px solid #eee;
  text-align: center;
}

.submission-table th {
  background-color: #f4f8fb;
  color: #333;
}

.accepted {
  color: green;
  font-weight: bold;
}

.failed {
  color: red;
  font-weight: bold;
}

.status-text {
  font-weight: bold;
}

.status-text[data-status="AC"] {
  color: #00a200; /* AC：绿色 */
}

.status-text:not([data-status="AC"]) {
  color: #e53935; /* 其他：红色 */
}

.submission-table tr:last-child td {
  border-bottom: none;
}

</style>
