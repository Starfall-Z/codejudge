<template>
  <div class="manage-container">
    <!-- ✅ 同一行的“返回主页” 和 “新增题目”按钮 -->
    <div class="header-bar">
      <h2 class="title">题库管理 🔨</h2>
      <div class="header-actions">
        <button class="add-btn" @click="showAddForm = true">新增题目</button>
        <button class="back-btn" @click="$router.push('/dashboard')">返回主页</button>
      </div>
    </div>

    <table class="problem-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>标题</th>
          <th>时间限制</th>
          <th>内存限制</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="problem in problems" :key="problem.id">
          <td>{{ problem.id }}</td>
          <td>{{ problem.title }}</td>
          <td>{{ problem.timeLimit }}</td>
          <td>{{ problem.memoryLimit }}</td>
          <td>
            <button class="edit-btn" @click="edit(problem)">✏️ 修改</button>
            <button class="delete-btn" @click="remove(problem.id)">🚮 删除</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- 添加/修改弹窗 -->
    <div v-if="showAddForm" class="form-dialog">
      <h3>{{ isEdit ? '修改题目' : '新增题目' }}</h3>
      <input v-model="form.title" placeholder="标题" />
      <input v-model="form.timeLimit" type="number" placeholder="时间限制（ms）" />
      <input v-model="form.memoryLimit" type="number" placeholder="内存限制（MB）" />
      <textarea v-model="form.description" placeholder="题目描述"></textarea>
      <textarea v-model="form.input" placeholder="输入说明"></textarea>
      <textarea v-model="form.output" placeholder="输出说明"></textarea>

      <div class="dialog-actions">
        <button class="submit-btn" @click="submit">{{ isEdit ? '保存' : '提交' }}</button>
        <button class="cancel-btn" @click="showAddForm = false">取消</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      problems: [],
      showAddForm: false,
      isEdit: false,
      form: {
        id: null,
        title: '',
        timeLimit: '',
        memoryLimit: '',
        description: '',
        input: '',
        output: ''
      }
    };
  },
  mounted() {
    this.loadProblems();
  },
  methods: {
    async loadProblems() {
      const res = await fetch('/api/problems');
      const data = await res.json();
      this.problems = data.data;
    },
    edit(problem) {
      this.form = { ...problem };
      this.isEdit = true;
      this.showAddForm = true;
    },
    async remove(id) {
      if (!confirm("确认删除？")) return;
      await fetch(`/api/problems/${id}`, { method: 'DELETE' });
      this.loadProblems();
    },
    async submit() {
      const method = this.isEdit ? 'PUT' : 'POST';
      const url = this.isEdit ? `/api/problems/${this.form.id}` : '/api/problems';
      await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(this.form)
      });
      this.showAddForm = false;
      this.loadProblems();
    }
  }
};
</script>

<style scoped>
:global(body) {
  margin: 0;
  padding: 0;
  min-height: 100vh;
  background: linear-gradient(to right, #74ebd5, #acb6e5);
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
}

.manage-container {
  padding: 40px;
  max-width: 1000px;
  margin: 60px auto;
  background: rgba(255, 255, 255, 0.33); /* 毛玻璃白色背景 */
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-radius: 16px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

/* ✅ 顶部按钮一行排布 */
.header-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.back-btn {
  background-color: #f28500;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
}

.add-btn {
  background-color: #409eff;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
}

.problem-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  background-color: transparent;
}

.problem-table th,
.problem-table td {
  padding: 14px 16px;
  border: none; /* ❌移除所有边框 */
  color: #000;
}

.problem-table th {
  font-weight: bold;
}

/* ✅ 精致的修改删除按钮 */
.edit-btn {
  background: #409eff;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  margin-right: 8px;
  cursor: pointer;
}

.edit-btn:hover {
  opacity: 0.9;
}

.delete-btn {
  background: #e75c4c;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  cursor: pointer;
}

.delete-btn:hover {
  opacity: 0.9;
}

.form-dialog {
  margin-top: 24px;
  padding: 24px;
  border-radius: 16px;
}

textarea, input {
  width: 97%;
  margin-bottom: 16px;
  padding: 10px;
  border-radius: 8px;
  border: 1px solid #ccc;
}

/* ✅ 表单底部按钮 */
.dialog-actions {
  display: flex;
  justify-content: center;
  transform: translateX(-1.2%);
  gap: 20px;
  margin-top: 20px;
}

.submit-btn {
  background: #409eff;
  color: white;
  border: none;
  padding: 8px 18px;
  border-radius: 6px;
  font-weight: bold;
}

.submit-btn:hover {
  background-color: #337ecc;
}

.cancel-btn {
  background: #f28500;
  color: white;
  border: none;
  padding: 8px 18px;
  border-radius: 6px;
  font-weight: bold;
}

.cancel-btn:hover {
  background-color: #d96c00;
}

.problem-table th {
  background-color: rgba(255, 255, 255, 0.2);
  color: #333;
  font-weight: bold;
}

.add-btn:hover {
  background-color: #337ecc;
}

.back-btn:hover {
  background-color: #d96c00;
}

.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 16px; /* 🔥 按钮之间空出距离 */
}
.problem-table th,
.problem-table td {
  text-align: center;
  vertical-align: middle;
}

</style>