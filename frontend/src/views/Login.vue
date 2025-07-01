<template>
  <div class="login-container">
    <div class="login-card">
      <h2>欢迎登录 CodeJudge 🚀</h2>
      <form @submit.prevent="login">
        <input v-model="username" placeholder="请输入用户名" />
        <input v-model="password" type="password" placeholder="请输入密码" />
        <button type="submit">登录</button>
        <p v-if="message" class="error">{{ message }}</p >
      </form>
      <p class="switch-link">
        还没有账号？
        <router-link to="/register">立即注册</router-link>
      </p >
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      username: '',
      password: '',
      message: ''
    };
  },
  methods: {
    async login() {
      try {
        const res = await fetch('http://localhost:8080/api/user/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
          body: new URLSearchParams({
            username: this.username,
            password: this.password
          })
        });
        const data = await res.json();
        if (data.success) {
          localStorage.setItem('username', this.username);
          localStorage.setItem('role', data.role);  // 👈 这行新增
          this.$router.push('/dashboard');
        } else {
          this.message = data.message;
        }
      } catch (e) {
        this.message = '请求失败';
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(to right, #74ebd5, #acb6e5);
}

.login-card {
  padding: 50px 40px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.15); /* 半透明白 */
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.25);
  backdrop-filter: blur(16px); /* 毛玻璃核心 */
  -webkit-backdrop-filter: blur(16px); /* Safari 兼容 */
  border: 1px solid rgba(255, 255, 255, 0.3);
  width: 100%;
  max-width: 360px;
  text-align: center;
  color: #000; /* 白色文字更清爽 */
}

.login-card h2 {
  margin-bottom: 24px;
  color: #333;
}

.login-card input {
  display: block;
  width: 93%;
  padding: 14px 16px;
  margin-bottom: 24px;
  border: 1px solid #ccc;
  border-radius: 10px;
  outline: none;
  font-size: 15px;
  transition: border-color 0.3s;
}

.login-card input:focus {
  border-color: #409eff;
}

.login-card button {
  width: 45%;
  padding: 12px;
  font-size: 16px;
  border-radius: 30px;
  color: #fff;
  background: rgba(64, 158, 255, 0.9); /* 半透明蓝色 */
  border: 1px solid rgba(96, 208, 255, 0.1); /* 毛玻璃边框 */
  backdrop-filter: blur(12px); /* 背景模糊 */
  -webkit-backdrop-filter: blur(12px);
  box-shadow: 0 8px 24px rgba(0, 0, 255, 0.1); /* 柔和阴影 */
  cursor: pointer;
  transition: all 0.3s ease;
}

.login-card button:hover {
  background: rgba(64, 158, 255, 0.35); /* hover 更亮一点 */
  box-shadow: 0 12px 30px rgba(64, 158, 255, 0.1);
  transform: translateY(-1px);
}

.login-card button:active {
  transform: scale(0.97);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.error {
  color: red;
  font-size: 14px;
  margin-top: 10px;
}

.switch-link {
  margin-top: 16px;
  font-size: 14px;
}
.switch-link a {
  color: #409eff;
  text-decoration: none;
}
.switch-link a:hover {
  text-decoration: underline;
}
.login-card input::placeholder {
  color: #333; /* 改成黑色 */
}
</style>