<template>
  <div>
    <h2>Controle por Dia</h2>
    <form class="grid" @submit.prevent="create">
      <div>
        <label>Data</label>
        <input type="date" v-model="data" required />
      </div>
      <div>
        <label>Capacidade Máxima</label>
        <input type="number" v-model.number="capacidadeMaxima" min="1" placeholder="480" />
      </div>
      <button type="submit">Cadastrar Capacidade</button>
    </form>
  </div>
</template>

<script lang="ts">
import { ref } from 'vue'
import { tiposApi } from '../services/api'
import { useAuthStore } from '../stores/auth'

export default {
  setup() {
    const data = ref('')
    const capacidadeMaxima = ref<number | null>(480)
    const auth = useAuthStore()

    async function create() {
      if (!auth.isAdmin()) return alert('Somente ADMIN')
      await tiposApi.createControleDia({
        data: data.value,
        capacidadeMaxima: capacidadeMaxima.value || undefined,
      })
      data.value = ''
      capacidadeMaxima.value = 480
      alert('Capacidade cadastrada')
    }

    return { data, capacidadeMaxima, create }
  },
}
</script>

<style scoped>
.grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(160px, 1fr));
  gap: 12px;
  margin-bottom: 16px;
}
</style>
