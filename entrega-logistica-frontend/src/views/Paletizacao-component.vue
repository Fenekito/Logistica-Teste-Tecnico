<template>
  <div>
    <h2>Paletização</h2>
    <ul>
      <li v-for="p in list" :key="p.id">{{ pretty(p.nome) }} (id: {{ p.id }})</li>
    </ul>
  </div>
</template>

<script lang="ts">
import { ref, onMounted } from 'vue'
import { tiposApi } from '../services/api'

export default {
  setup() {
    const list = ref<Array<{ id: number; nome: string }>>([])
    onMounted(async () => {
      list.value = await tiposApi.listPaletizacao()
    })

    function pretty(n: string) {
      if (!n) return n
      if (n.includes('NAO')) return 'Não paletizado'
      return 'Paletizado'
    }
    return { list, pretty }
  },
}
</script>
