import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login-component.vue'
import Dashboard from '../views/Dashboard-component.vue'
import AgendamentoComponent from '../views/Agendamento-component.vue'
import PaletizacaoComponent from '../views/Paletizacao-component.vue'
import CaminhaoComponent from '../views/Caminhao-component.vue'
import RelatorioComponent from '../views/Relatorio-component.vue'
import FaixaComponent from '../views/Faixa-component.vue'
import ControleDiaComponent from '../views/ControleDia-component.vue'
import { useAuthStore } from '../stores/auth'

//Criação de Rotas

const routes = [
  { path: '/', component: Dashboard, meta: { requiresAuth: true } },
  { path: '/login', component: Login },
  { path: '/agendamento', component: AgendamentoComponent, meta: { requiresAuth: true } },
  { path: '/paletizacao', component: PaletizacaoComponent, meta: { requiresAuth: true } },
  { path: '/caminhao', component: CaminhaoComponent, meta: { requiresAuth: true } },
  { path: '/relatorios', component: RelatorioComponent, meta: { requiresAuth: true } },
  { path: '/faixas', component: FaixaComponent, meta: { requiresAuth: true } },
  { path: '/controle-dia', component: ControleDiaComponent, meta: { requiresAuth: true } },
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  const auth = useAuthStore()
  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return next('/login')
  }
  next()
})

export default router
