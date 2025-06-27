<template>
  <div class="register-container">
    <div class="register-card">
      <h2>欢迎注册 LiteCode 🚀</h2>
      <form @submit.prevent="handleRegister">
        <input v-model="username" placeholder="请输入用户名" required />
        <input v-model="password" type="password" placeholder="请输入密码" required />
        <select v-model="role">
          <option value="USER">普通用户</option>
          <option value="ADMIN">管理员</option>
        </select>
        <button type="submit">注册</button>
        <p class="error" v-if="errorMessage">{{ errorMessage }}</p >
        <p class="success" v-if="successMessage">{{ successMessage }}</p >
      </form>
      <p class="switch-link">
        已有账号？
        <router-link to="/login">立即登录</router-link>
      </p >
    </div>
  </div>
</template>

<script>
export default {
  name: "UserRegister",
  data() {
    return {
      username: '',
      password: '',
      role: 'USER', // 默认角色
      errorMessage: '',
      successMessage: ''
    };
  },
  methods: {
    async handleRegister() {
      this.errorMessage = '';
      this.successMessage = '';
      try {
        const res = await fetch('http://localhost:8080/api/user/register', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          body: new URLSearchParams({
            username: this.username,
            password: this.password,
            role: this.role
          })
        });
        const data = await res.json();
        if (data.success) {
          this.successMessage = "注册成功，正在跳转...";
          setTimeout(() => this.$router.push("/login"), 1500);
        } else {
          this.errorMessage = data.message || "注册失败";
        }
      } catch (err) {
        this.errorMessage = "请求失败";
        console.error('详细错误:', err);
      }
    }
  }
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(to right, #74ebd5, #acb6e5);
  overflow: hidden;
}

.register-card {
  padding: 50px 45px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.18);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.25);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  width: 100%;
  max-width: 420px;
  text-align: center;
  color: #000;
  font-size: 16px;
}

.register-card h2 {
  margin-bottom: 36px;
  font-size: 24px;
  color: #000;
}

.register-card input {
  display: block;
  width: 93%;
  padding: 14px 14px;
  margin-bottom: 24px;

  /* 白色背景 + 不透明 */
  background-color: #ffffff;
  border: 1px solid #ccc;
  border-radius: 8px;

  outline: none;
  font-size: 14px;
  color: #333;
  transition: border-color 0.3s;
}

.register-card input::placeholder {
  color: #333;
}

.register-card select {
  background-color: rgba(255, 255, 255, 0.2);
  color: #333;
}

.register-card select option {
  color: #333;
}

.register-card input:focus,
.register-card select:focus {
  border-color: #74ebd5;
}

.register-card button {
  width: 45%;
  padding: 12px;
  background: rgba(64, 158, 255, 0.9); /* 半透明蓝色 */
  border: 1px solid rgba(96, 208, 255, 0.1); /* 毛玻璃边框 */
  backdrop-filter: blur(12px); /* 背景模糊 */
  -webkit-backdrop-filter: blur(12px);
  box-shadow: 0 8px 24px rgba(64, 158, 255, 0.1); /* 柔和阴影 */
  color: white;
  border-radius: 45px;
  font-size: 17px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.register-card button:hover {
    background: rgba(64, 158, 255, 0.35); /* hover 更亮一点 */
    box-shadow: 0 12px 30px rgba(64, 158, 255, 0.1);
    transform: translateY(-1px);
}

.error {
  color: #ff0000;
  font-size: 14px;
  margin-top: 10px;
}

.success {
  color: #009900;
  font-size: 14px;
  margin-top: 10px;
}

.switch-link {
  margin-top: 20px;
  font-size: 14px;
}

.switch-link a {
  color: #409eff;
  text-decoration: none;
  font-weight: normal;
}

.switch-link a:hover {
  text-decoration: underline;
}

.register-card select {
  display: block;
  width: 100%;
  padding: 12px 9px;
  margin-bottom: 24px;
  border: 1px solid #ccc;
  border-radius: 10px;
  outline: none;
  font-size: 14px;
  transition: border-color 0.3s;
  background-color: #fff;
}

.register-card select:focus {
  border-color: #409eff;
}

</style>