<template>
  <nav class="nav">
    <div class="brand">Logística Rede Krill</div>
    <ul>
      <li v-if="isAuth"><router-link to="/">Dashboard</router-link></li>
      <li v-if="isAuth"><router-link to="/agendamento">Agendamentos</router-link></li>
      <li v-if="isAuth"><router-link to="/relatorios">Relatórios</router-link></li>
      <li v-if="isAuth && isAdmin"><router-link to="/faixas">Faixas</router-link></li>
      <li v-if="isAuth && isAdmin">
        <router-link to="/controle-dia">Controle do Dia</router-link>
      </li>
      <li v-if="isAuth && isAdmin"><router-link to="/caminhao">Caminhões</router-link></li>
      <li v-if="isAuth && isAdmin"><router-link to="/paletizacao">Paletizações</router-link></li>
    </ul>
    <div class="right">
      <span v-if="user">Olá, {{ user.email }}</span>
      <button v-if="isAuth" @click="logout">Sair</button>
      <router-link v-else to="/login">Entrar</router-link>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useAuthStore } from '../stores/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

const isAuth = computed(() => auth.isAuthenticated)
const isAdmin = computed(() => auth.isAdmin())
const user = computed(() => auth.user)

function logout() {
  auth.logout()
  router.push('/login')
}
</script>
