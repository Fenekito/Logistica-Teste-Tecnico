import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { authApi } from '../services/api'

interface UserInfo {
  email?: string
  roles?: string[]
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const user = ref<UserInfo | null>(token.value ? parseJwt(token.value) : null)
  const isAuthenticated = computed(() => !!token.value)

  function parseJwt(raw: string | null) {
    if (!raw) return null
    try {
      const parts = raw.split('.')
      if (parts.length !== 3) return null
      const payload = JSON.parse(atob(parts[1].replace(/-/g, '+').replace(/_/g, '/')))
      return { email: payload.sub, roles: payload.roles ?? [] }
    } catch {
      return null
    }
  }

  async function login(email: string, password: string) {
    const res = await authApi.login({ email, password })
    // res.token is raw jwt
    token.value = res.token
    localStorage.setItem('token', res.token)
    user.value = parseJwt(res.token)
    return res
  }

  function logout() {
    token.value = null
    user.value = null
    localStorage.removeItem('token')
  }

  function isAdmin() {
    return user.value?.roles?.includes('ROLE_ADMIN')
  }

  return { token, user, isAuthenticated, login, logout, isAdmin }
})
