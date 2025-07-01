<template>
  <div class="review-page">
    <div class="review-container">
      <div class="header">
        <h2>题目回顾📝</h2>
        <button class="back-btn" @click="goBack">返回🏃‍</button>
      </div>

      <div v-if="loading" class="status">正在加载题目回顾...</div>
      <div v-else-if="records.length === 0" class="status">暂无做过的题目</div>

      <div v-else class="record-list">
        <div class="record-card" v-for="r in records" :key="r.id">
          <div class="record-info">
            <h3>{{ problemMap[r.problemId] || `题目 #${r.problemId}` }}</h3>
            <p>提交语言：{{ r.language || '未知' }}｜状态：
              <span class="status-text" :data-status="r.status.toUpperCase()">
                {{ r.status }}
              </span>
            </p>
            <p class="time">提交时间：{{ formatTime(r.submitTime) }}</p>
          </div>

          <div class="record-actions">
            <button @click="goToProblem(r.problemId)">查看详情</button>
            <button @click="viewCode(r)">查看代码</button>
          </div>
        </div>
      </div>
    </div>

    <!-- ✅ 新增代码弹窗 -->
    <div v-if="selectedCode" class="modal">
      <div class="modal-content">
        <h3>代码内容</h3>
        <pre>{{ selectedCode }}</pre>
        <button @click="selectedCode = ''">关闭</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Review",
  data() {
    return {
      records: [],
      loading: true,
      problemMap: {},
      selectedCode: "" // ✅ 用于代码弹窗内容
    };
  },
  async mounted() {
    const username = localStorage.getItem("username");
    try {
      const res = await fetch(`/api/submissions/${username}`);
      const data = await res.json();
      this.records = data;

      const uniqueIds = [...new Set(data.map(r => r.problemId))];
      await Promise.all(uniqueIds.map(async id => {
        const res = await fetch(`/api/problems/${id}`);
        const result = await res.json();
        this.problemMap[id] = result.data.title;
      }));
    } catch (err) {
      console.error("加载失败", err);
    } finally {
      this.loading = false;
    }
  },
  methods: {
    goBack() {
      this.$router.push("/dashboard");
    },
    goToProblem(id) {
      this.$router.push(`/problems/${id}`);
    },
    viewCode(record) {
      this.selectedCode = record.code || "（无代码内容）";
    },
    formatTime(timeStr) {
      return new Date(timeStr).toLocaleString();
    }
  }
};
</script>


<style scoped>
.review-page {
  min-height: 100vh;
  background: linear-gradient(to right, #74ebd5, #acb6e5);
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 60px 20px;
}

.review-container {
  background: rgba(255, 255, 255, 0.33); /* 毛玻璃白色背景 */
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 40px;
  max-width: 960px;
  width: 100%;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
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

.record-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(360px, 1fr));
  gap: 20px;
}

.record-card {
  background-color: #fdfdfd;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
}

.record-card h3 {
  margin-bottom: 10px;
  color: #34495e;
}

.record-card p {
  margin-bottom: 8px;
  font-size: 14px;
  color: #555;
}

.record-card .time {
  font-style: italic;
  color: #999;
}

.record-card button {
  margin-top: 10px;
  padding: 8px 16px;
  background-color: #409eff;
  border: none;
  color: white;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
}

.record-card button:hover {
  background-color: #337ecc;
}

.record-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  gap: 12px; /* 可选，让两个按钮之间不紧贴 */
}

.accepted {
  color: green;
  font-weight: bold;
}
.failed {
  color: red;
  font-weight: bold;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0,0,0,0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}
.modal-content {
  background: white;
  padding: 28px;
  border-radius: 12px;
  max-width: 100%;
  max-height: 80%;
  overflow: auto;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.3);
}
.modal-content pre {
  background: #f4f4f4;
  padding: 16px;
  border-radius: 8px;
  overflow-x: auto;
}

.record-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
}

.record-info {
  flex: 1;
}

.record-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: center;
  gap: 10px;
}

/* ✅ Style 部分 */
.status-text {
  font-weight: bold;
}

.status-text[data-status="AC"] {
  color: #00a200;
}

.status-text:not([data-status="AC"]) {
  color: #e53935;
}
</style>
