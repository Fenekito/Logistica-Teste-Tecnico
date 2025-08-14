<template>
  <div>
    <h2>Relatórios de Agendamentos</h2>
    <form class="filters" @submit.prevent="load">
      <div>
        <label>Início</label>
        <input type="date" v-model="inicio" required />
      </div>
      <div>
        <label>Fim</label>
        <input type="date" v-model="fim" required />
      </div>
      <button type="submit">Carregar</button>
    </form>

    <table v-if="list.length" class="table">
      <thead>
        <tr>
          <th>Data/Hora</th>
          <th>Pedido</th>
          <th>Fornecedor</th>
          <th>Faixa</th>
          <th>Caminhão</th>
          <th>Paletização</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="a in list" :key="a.id">
          <td>{{ formatDateTime(a.criadoEm) }}</td>
          <td>{{ a.idPedido }}</td>
          <td>{{ a.fornecedor }}</td>
          <td>{{ a.faixaDescricao }}</td>
          <td>{{ a.tipoCaminhao }}</td>
          <td>{{ prettyPaletizacao(a.tipoPaletizacao) }}</td>
          <td>{{ a.status }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script lang="ts">
import { ref } from 'vue'
import { agendamentoApi, type AgendamentoListItem } from '../services/api'

export default {
  setup() {
    const inicio = ref<string>('')
    const fim = ref<string>('')
    const list = ref<AgendamentoListItem[]>([])

    function prettyPaletizacao(name: string) {
      if (!name) return name
      if (name.toUpperCase().includes('NAO')) return 'Não paletizado'
      if (name.toUpperCase().includes('PALET')) return 'Paletizado'
      return name
    }

    function formatDateTime(s: string) {
      return s ? new Date(s).toLocaleString() : ''
    }

    async function load() {
      if (!inicio.value || !fim.value) return
      list.value = await agendamentoApi.listByPeriod(inicio.value, fim.value)
    }

    return { inicio, fim, list, load, prettyPaletizacao, formatDateTime }
  },
}
</script>

<style scoped>
.filters {
  display: grid;
  grid-template-columns: repeat(3, minmax(140px, 240px));
  gap: 12px;
  align-items: end;
  margin-bottom: 16px;
}
</style>
