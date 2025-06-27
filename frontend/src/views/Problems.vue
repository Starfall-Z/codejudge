<template>
  <div class="page-bg"> <!-- ✅ 渐变背景层 -->
    <div class="problem-list-container"> <!-- ✅ 你的原卡片内容保持不变 -->
      <div class="header">
        <h2>题目列表📚</h2>
        <button class="back-btn" @click="goBack">返回🏃‍</button>
      </div>

      <div v-if="loading" class="loading">正在加载题目...</div>
      <div v-else-if="problems.length === 0" class="empty">暂无题目，请稍后再试。</div>
      <div v-else class="problem-cards">
        <div class="problem-card" v-for="problem in problems" :key="problem.id">
          <h3>{{ problem.title }}</h3>
          <p>{{ truncate(problem.description, 80) }}</p >
          <button @click="goToProblem(problem.id)">进入答题</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Problems",
  data() {
    return {
      problems: [],
      loading: true
    };
  },
  mounted() {
     fetch("/api/problems")
        .then(res => res.json())
        .then(data => {
          console.log("接口返回数据：", data);
          this.problems = data.data || [];  // 正确提取 data 字段
          this.loading = false;
        })
        .catch(err => {
          console.error("加载题目失败：", err);
          this.loading = false;
        });
  },
  methods: {
    goToProblem(id) {
      this.$router.push(`/problems/${id}`);
    },
    goBack() {
      this.$router.push('/dashboard');
    },
    truncate(text, length) {
      return text.length > length ? text.slice(0, length) + '...' : text;
    }
  }
};
</script>

<style scoped>
.problem-list-container {
  padding: 40px;
  max-width: 960px;
  margin: auto;
  background:linear-gradient(to right, #77e5d6,#9bbe5);
  border-radius: 12px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

h2 {
  font-size: 24px;
  color: #2c3e50;
}

.back-btn {
  padding: 8px 14px;
  background-color: #f28500;
  border: none;
  color: white;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
}

.back-btn:hover {
  background-color: #d96c00;
}

.loading,
.empty {
  font-size: 16px;
  color: #666;
  text-align: center;
  margin-top: 60px;
}

.problem-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
  gap: 24px;
}

.problem-card {
  background: rgba(255, 255, 255, 0.25); /* 半透明白 */
  backdrop-filter: blur(18px);           /* 毛玻璃模糊 */
  -webkit-backdrop-filter: blur(18px);   /* Safari 支持 */
  padding: 24px;
  border-radius: 14px;
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.15);
  transition: transform 0.2s ease;
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: #000;
}

.problem-card:hover {
  transform: translateY(-4px);
}

.problem-card h3 {
  margin-bottom: 12px;
  color: #34495e;
  font-size: 18px;
}

.problem-card p {
  font-size: 14px;
  color: #666;
  margin-bottom: 20px;
  line-height: 1.5;
  min-height: 40px;
}

.problem-card button {
  padding: 10px 18px;
  background-color: #409eff;
  border: none;
  color: white;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.3s ease;
}

.problem-card button:hover {
  background-color: #337ecc;
}

.page-bg {
  min-height: 100vh;
  background: linear-gradient(to right, #74ebd5, #acb6e5);
  padding: 40px 0;
}

</style>