<template>
  <div class="dashboard-container">
    <div class="dashboard-card">
      <h2>欢迎回来 👋</h2>
      <p class="subtitle">请选择你要进行的操作</p >

      <div class="action-buttons">
        <button @click="startProblem">开始做题🚀</button>
        <button @click="goToSubmissions">提交记录📖</button>
        <button @click="goToReview">题目回顾👀</button>
        <button @click="goToProfile">用户中心👤</button> <!-- ✅ 新增 -->
        <!-- ✅ 管理员才可见 -->
        <button v-if="role === 'ADMIN'" @click="goToManageProblems">题库管理🛠️</button>
        <!-- ✅ 管理员才可见 -->
        <button v-if="role === 'ADMIN'" @click="goToManageSubmissions">记录管理🗃️</button>
        <!-- ✅ 仅普通用户才可见 -->
        <button v-if="role === 'USER'" @click="goToUserGuide">用户手册📘</button>
        <button class="logout" @click="logout">退出登录🏃‍</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserDashboard",
  data() {
    return {
      username: localStorage.getItem('username') || '用户',
      role: localStorage.getItem('role') || 'USER',
      selectedCode:''
    };
  },
  methods: {
    startProblem() {
      this.$router.push('/problems');
    },
    goToSubmissions() {
      this.$router.push('/submissions');
    },
    goToReview() {
      this.$router.push('/review');
    },
    goToProfile() {
      this.$router.push('/profile');  // 👈 用户中心跳转路径
    },
    goToManageProblems() {
      this.$router.push('/admin/problems');  // 👈 自定义路由路径
    },
    goToManageSubmissions() {
      this.$router.push('/admin/submissions');
    },
    goToUserGuide() {
      this.$router.push('/user-guide');
    },
    viewCode(record){
      this.selectedCode = record.code||"// 无代码内容";
    },
    logout() {
      localStorage.clear();
      this.$router.push('/login');
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(to right, #74ebd5, #acb6e5);
  padding: 20px;
}

.dashboard-card {
  padding: 60px 55px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.2);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.25);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  width: 100%;
  max-width: 520px;
  text-align: center;
  color: #000; /* 全部黑色字体 */
  font-size: 16px;
}

.dashboard-card h2 {
  font-size: 28px;
  margin-bottom: 12px;
  color: #000;
}

.dashboard-card .subtitle {
  font-size: 16px;
  color: #000;
  margin-bottom: 32px;
}

.action-buttons button {
  display: block;
  width: 100%;
  padding: 14px;
  margin-bottom: 18px;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  background-color: #ffffff;
  color: #000;
  letter-spacing: 0.5px;
}

.action-buttons button:hover {
  background-color: #cfcfcf;
  transform: translateY(-1px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
}

.logout {
  background-color: #f56c6c;
  color: white;
}

.logout:hover {
  background-color: #dd4b39;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0,0,0,0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 12px;
  max-width: 90%;
  max-height: 80%;
  overflow: auto;
  white-space: pre-wrap;
}

.modal-content pre {
  background: #f6f6f6;
  padding: 10px;
  border-radius: 6px;
  overflow-x: auto;
}
</style>