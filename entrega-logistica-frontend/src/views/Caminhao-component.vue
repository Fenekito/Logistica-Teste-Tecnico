<template>
  <div>
    <h2>Tipos de Caminhão</h2>
    <form @submit.prevent="create">
      <input v-model="nome" placeholder="nome (ex: truck)" />
      <button>Cadastrar</button>
    </form>
    <ul>
      <li v-for="c in list" :key="c.id">{{ c.nome }}</li>
    </ul>
  </div>
</template>

<script lang="ts">
import { ref, onMounted } from 'vue'
import type { AxiosError } from 'axios'
import { tiposApi } from '../services/api'
import { useAuthStore } from '../stores/auth'

export default {
  setup() {
    const nome = ref('')
    const list = ref<Array<{ id: number; nome: string }>>([])
    const auth = useAuthStore()

    onMounted(async () => {
      list.value = await tiposApi.listCaminhoes()
    })

    function extractErrorMessage(e: unknown): string {
      const ax = e as AxiosError<{ message?: string }>
      return ax?.response?.data?.message ?? 'Erro'
    }

    async function create() {
      if (!auth.isAdmin()) return alert('Somente ADMIN')
      try {
        await tiposApi.createCaminhao({ nome: nome.value })
        list.value = await tiposApi.listCaminhoes()
        nome.value = ''
      } catch (e: unknown) {
        alert(extractErrorMessage(e))
      }
    }

    return { nome, list, create }
  },
}
</script>
