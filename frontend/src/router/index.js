import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Dashboard from '../views/Dashboard.vue'
import Problems from '../views/Problems.vue'
import ProblemDetail from '../views/ProblemDetail.vue'
import Submissions from '../views/Submissions.vue'
import Review from '../views/Review.vue'
import Manage from '../views/ProblemManage.vue'
import UserProfile from '../views/UserProfile.vue'
import AdminSubmissions from '../views/AdminSubmissions.vue'
import UserGuide from '../views/UserGuide.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/dashboard', component: Dashboard },
  { path: '/problems', component: Problems },
  { path: '/problems/:id', component: ProblemDetail, props: true },
  { path: '/submissions', component: Submissions },
  { path: '/review', component: Review },
  { path: '/admin/problems',component: Manage },
  { path: '/profile',component: UserProfile},
  { path: '/admin/submissions',component: AdminSubmissions},
  { path: '/user-guide',component: UserGuide}
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router