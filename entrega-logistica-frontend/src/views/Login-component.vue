<template>
  <div class="login">
    <h2>Login</h2>
    <form @submit.prevent="doLogin">
      <div>
        <label>Email</label>
        <input v-model="email" type="email" required />
      </div>
      <div>
        <label>Senha</label>
        <input v-model="password" type="password" required />
      </div>
      <button>Entrar</button>
      <div v-if="error" class="error">{{ error }}</div>
    </form>
  </div>
</template>

<script lang="ts">
import { ref } from 'vue'
import { useAuthStore } from '../stores/auth'
import { useRouter } from 'vue-router'

export default {
  setup() {
    const email = ref('')
    const password = ref('')
    const error = ref<string | null>(null)
    const auth = useAuthStore()
    const router = useRouter()

    async function doLogin() {
      try {
        error.value = null
        await auth.login(email.value, password.value)
        router.push('/')
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
      } catch (e: any) {
        error.value = e?.response?.data?.message ?? 'Erro ao logar'
      }
    }

    return { email, password, doLogin, error }
  },
}
</script>

<style scoped>
.error {
  color: red;
  margin-top: 8px;
}
</style>
