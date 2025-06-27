<template>
  <div class="profile-container">
    <button class="side-back-button" @click="goBack">返回 🏃‍</button>
    <div class="profile-card">
      <h2>欢迎来到用户中心 🚀</h2>
      <p class="subtitle">修改用户名 / 密码</p>

      <form @submit.prevent="handleUsernameChange">
        <input v-model="newUsername" placeholder="新用户名" required />
        <button type="submit">修改用户名</button>
      </form>

      <form @submit.prevent="handlePasswordChange" style="margin-top: 20px;">
        <input v-model="oldPassword" type="password" placeholder="原密码" required />
        <input v-model="newPassword" type="password" placeholder="新密码" required />
        <button type="submit">修改密码</button>
      </form>

      <p v-if="message" class="message">{{ message }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserProfile",
  data() {
    return {
      newUsername: '',
      oldPassword: '',
      newPassword: '',
      message: ''
    };
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    async handleUsernameChange() {
      this.message = '';
      const current = localStorage.getItem('username');
      try {
        const res = await fetch("http://localhost:8080/api/user/rename", {
          method: "POST",
          headers: { "Content-Type": "application/x-www-form-urlencoded" },
          body: new URLSearchParams({
            oldUsername: current,
            newUsername: this.newUsername
          })
        });
        const data = await res.json();
        if (data.success) {
          this.message = "用户名修改成功，请重新登录";
          localStorage.clear();
          setTimeout(() => this.$router.push('/login'), 1500);
        } else {
          this.message = data.message || "修改失败";
        }
      } catch (err) {
        this.message = "请求失败";
      }
    },
    async handlePasswordChange() {
      this.message = '';
      const username = localStorage.getItem('username');
      try {
        const res = await fetch("http://localhost:8080/api/user/reset", {
          method: "POST",
          headers: { "Content-Type": "application/x-www-form-urlencoded" },
          body: new URLSearchParams({
            username,
            oldPassword: this.oldPassword,
            newPassword: this.newPassword
          })
        });
        const data = await res.json();
        if (data.success) {
          this.message = "密码修改成功，请重新登录";
          localStorage.clear();
          setTimeout(() => this.$router.push('/login'), 1500);
        } else {
          this.message = data.message || "修改失败";
        }
      } catch (err) {
        this.message = "请求失败";
      }
    }
  }
};
</script>

<style scoped>
.profile-container {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(to right, #74ebd5, #acb6e5);
}

.profile-card {
  padding: 50px;
  background: rgba(255, 255, 255, 0.18);
  border-radius: 24px;
  transform: translateX(-2%);
  backdrop-filter: blur(20px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.2);
  text-align: center;
  color: #000;
}

.subtitle {
  margin-top: 10px;
  font-size: 16px;
  color: #333;
}

.profile-card input {
  display: block;
  width: 100%;
  padding: 12px;
  margin: 16px 0;
  border: 1px solid #ccc;
  border-radius: 12px;
  background-color: #fff;
  transform: translateX(-5%);
  font-size: 14px;
  color: #333;
}

.profile-card button {
  padding: 10px 20px;
  background-color: #409eff;
  color: #fff;
  border: none;
  border-radius: 30px;
  font-size: 15px;
  cursor: pointer;
  margin-top: 10px;
}

.profile-card button:hover {
  background-color: #337ecc;
}

.side-back-button {
  position: absolute;
  transform: translateX(-100%);
  right: 8%;
  top: 50%;
  background-color: #f28500;
  color: white;
  border: none;
  border-radius: 20px;
  padding: 10px 20px;
  font-size: 15px;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  transition: background-color 0.3s, transform 0.3s;
}

.side-back-button:hover {
  background-color: #d96c00;
}

.message {
  margin-top: 15px;
  color: #222;
  font-size: 14px;
}
</style>