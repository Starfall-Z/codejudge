<template>
  <div class="admin-submissions">
    <div class="submission-container">
      <div class="submission-header">
        <h2>📋 提交记录管理</h2>
        <button class="back-btn" @click="goBack">返回🏃‍</button>
      </div>
      <div v-if="submissions.length === 0" class="empty">暂无记录</div>

      <table v-else class="submission-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>用户</th>
            <th>题目编号</th>
            <th>语言</th>
            <th>状态</th>
            <th>提交时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="sub in submissions" :key="sub.id">
            <td>{{ sub.id }}</td>
            <td>{{ sub.username }}</td>
            <td>{{ sub.problemId }}</td>
            <td>{{ sub.language }}</td>
            <td :class="{ accepted: sub.status === 'Accepted', failed: sub.status !== 'Accepted' }">
              {{ sub.status }}
            </td>
            <td>{{ format(sub.submitTime) }}</td>
            <td>
              <button @click="viewCode(sub)">查看代码</button>
              <button class="danger" @click="deleteSubmission(sub.id)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 代码弹窗 -->
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
  name: "AdminSubmissions",
  data() {
    return {
      submissions: [],
      selectedCode: ''
    };
  },
  methods: {
    goBack() {
      this.$router.push('/dashboard');
    },
    async fetchAll() {
      try {
        const res = await fetch("http://localhost:8080/api/submissions/all");
        const data = await res.json();
        this.submissions = data;
      } catch (e) {
        alert("获取提交记录失败");
      }
    },
    viewCode(sub) {
      this.selectedCode = sub.code;
    },
    async deleteSubmission(id) {
      if (!confirm("确定要删除该记录吗？")) return;
      try {
        await fetch(`http://localhost:8080/api/submissions/${id}`, { method: "DELETE" });
        this.submissions = this.submissions.filter(s => s.id !== id);
      } catch {
        alert("删除失败");
      }
    },
    format(dateTime) {
      return dateTime.replace("T", " ");
    }
  },
  mounted() {
    this.fetchAll();
  }
};
</script>

<style scoped>
.admin-submissions {
  padding: 60px 20px;
  background: linear-gradient(to right, #74ebd5, #acb6e5);
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.submission-container {
  background: rgba(255, 255, 255, 0.33);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-radius: 16px;
  padding: 40px;
  max-width: 1000px;
  width: 100%;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.4);
  color: #000;
}

.empty {
  margin-top: 20px;
  font-size: 16px;
  text-align: center;
}

.submission-table {
  width: 100%;
  border-collapse: collapse;
}

.submission-table th,
.submission-table td {
  padding: 12px 16px;
  text-align: center;
  border-bottom: 1px solid #ddd;
}

.submission-table th {
  background-color: #f0f8ff;
  font-weight: bold;
  color: #222;
}

.submission-table tr:last-child td {
  border-bottom: none;
}

.accepted {
  color: green;
  font-weight: bold;
}

.failed {
  color: red;
  font-weight: bold;
}

button {
  margin: 0 5px;
  padding: 6px 12px;
  border: none;
  border-radius: 8px;
  background-color: #409eff;
  color: white;
  cursor: pointer;
  font-size: 13px;
}

button.danger {
  background-color: #e74c3c;
}

button:hover {
  opacity: 0.9;
}

.modal {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  width: 600px;
  max-height: 80vh;
  overflow-y: auto;
}
.submission-header {
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
</style>