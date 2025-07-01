<template>
  <div class="user-guide-page">
    <div class="guide-card">
      <div class="header">
        <h1>用户手册 📝</h1>
        <button class="back-btn" @click="goBack">返回</button>
      </div>

      <p>欢迎使用 <strong>CodeJudge</strong> 在线判题系统！以下是系统主要功能的使用说明：</p>

      <ul>
        <li>
          <strong>开始做题：</strong> 点击“开始做题”按钮进入题库页面，选择题目、编写代码并提交。
        </li>
        <li>
          <strong>提交记录：</strong> 查看历史提交，包括提交语言、判题方式以及结果。
        </li>
        <li>
          <strong>题目回顾：</strong> 可跳转至题面复习、再次练习，适合整理错题、并且可以查看之前提交的代码。
        </li>
        <li>
          <strong>用户中心：</strong> 支持查看用户信息与修改密码。
        </li>
        <li>
          <strong>退出登录：</strong> 清除登录信息并返回登录页面。
        </li>
        <li>
          <strong>状态解释：</strong> AC：代码正确。WA：错误输出。SE：语法错误。CE：无法编译。
        </li>
      </ul>

      <p>💡 <strong>Tips：</strong> 本系统支持 AI 智能判题与本地判题两种模式。</p>
      <p>🎉 祝您使用愉快，刷题顺利！</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserGuide",
  methods: {
    goBack() {
      this.$router.push("/dashboard");
    }
  }
};
</script>

<style scoped>
.user-guide-page {
  height: 100vh;
  background: linear-gradient(to right, #74ebd5, #acb6e5);
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: "Segoe UI", sans-serif;
  overflow: hidden;
  padding: 0; /* 去掉多余 padding */
}

.guide-card {
  background: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 50px 50px;
  max-width: 800px;
  width: 100%;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
  color: #333;
  font-size: 16px;
  line-height: 1.8;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.back-btn {
  background-color: #409eff;
  color: white;
  border: none;
  padding: 10px 18px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.2s;
}

.back-btn:hover {
  background-color: #337ecc;
}

ul {
  padding-left: 20px;
  margin-top: 16px;
}

li {
  margin-bottom: 12px;
}
</style>