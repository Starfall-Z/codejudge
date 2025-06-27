<template>
  <div class="review-page">
    <div class="review-container">
      <div class="header">
        <h2>题目回顾👀</h2>
        <button class="back-btn" @click="goBack">返回🏃‍</button>
      </div>

      <div v-if="loading" class="status">正在加载题目回顾...</div>
      <div v-else-if="records.length === 0" class="status">暂无做过的题目</div>

      <div v-else class="record-list">
        <div class="record-card" v-for="r in records" :key="r.id">
          <h3>{{ problemMap[r.problemId] || `题目 #${r.problemId}` }}</h3>
          <p>提交语言：{{ r.language || '未知' }}｜状态：<span :class="{ accepted: r.status === 'Accepted', failed: r.status !== 'Accepted' }">{{ r.status }}</span></p>
          <p class="time">提交时间：{{ formatTime(r.submitTime) }}</p>
          <button @click="goToProblem(r.problemId)">查看详情</button>
        </div>
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
      problemMap: {} // 存储 problemId => title
    };
  },
  async mounted() {
    const username = localStorage.getItem("username");
    try {
      const res = await fetch(`/api/submissions/${username}`);
      const data = await res.json();
      this.records = data;

      // 构建问题映射（每道题只请求一次）
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
  border-radius: 16px;
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

.accepted {
  color: green;
  font-weight: bold;
}
.failed {
  color: red;
  font-weight: bold;
}
</style>
