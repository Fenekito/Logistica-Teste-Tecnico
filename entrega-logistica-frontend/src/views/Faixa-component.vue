<template>
  <div>
    <h2>Faixas de Horário</h2>

    <div v-if="error" class="error-alert">{{ error }}</div>
    <div v-if="list.length >= 4" class="warn-alert">
      Limite atingido: já existem 4 faixas cadastradas. Remova alguma faixa para cadastrar novas.
    </div>

    <form class="grid" @submit.prevent="create">
      <div>
        <label>Início</label>
        <input type="time" v-model="inicio" required :disabled="list.length >= 4" />
      </div>
      <div>
        <label>Fim</label>
        <input type="time" v-model="fim" required :disabled="list.length >= 4" />
      </div>
      <div>
        <label>Descrição</label>
        <input v-model="descricao" placeholder="Ex: 08:00-10:00" :disabled="list.length >= 4" />
      </div>
      <div>
        <label>Capacidade Máxima</label>
        <input
          type="number"
          v-model.number="capacidade"
          min="1"
          placeholder="120"
          :disabled="list.length >= 4"
        />
      </div>
      <button
        type="submit"
        :disabled="list.length >= 4"
        :title="list.length >= 4 ? 'Limite de 4 faixas atingido' : ''"
      >
        Cadastrar Faixa
      </button>
      <button type="button" @click="criarPadrao4Faixas">Criar 4 faixas padrão (2h)</button>
    </form>

    <table class="table" v-if="list.length">
      <thead>
        <tr>
          <th>ID</th>
          <th>Descrição</th>
          <th>Início</th>
          <th>Fim</th>
          <th style="width: 1%">Ações</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="f in list" :key="f.id">
          <td>{{ f.id }}</td>
          <td>{{ f.descricao ?? formatHHMM(f.inicio) + '-' + formatHHMM(f.fim) }}</td>
          <td>{{ formatHHMM(f.inicio) }}</td>
          <td>{{ formatHHMM(f.fim) }}</td>
          <td>
            <button @click="remove(f.id)" title="Remover" style="background: #b91c1c">
              Remover
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script lang="ts">
import { onMounted, ref } from 'vue'
import { tiposApi } from '../services/api'
import { useAuthStore } from '../stores/auth'

export default {
  setup() {
    const inicio = ref('')
    const fim = ref('')
    const descricao = ref('')
    const capacidade = ref<number | null>(120)
    const list = ref<Array<{ id: number; inicio: string; fim: string; descricao?: string }>>([])
    const auth = useAuthStore()
    const error = ref<string | null>(null)

    onMounted(async () => {
      list.value = await tiposApi.listFaixas()
    })

    function parseMinutes(hhmm: string): number | null {
      // Realiza o parsing dos minutos
      const m = /^([01]?\d|2[0-3]):([0-5]\d)(?::([0-5]\d))?$/.exec(hhmm)
      if (!m) return null
      const h = Number(m[1])
      const min = Number(m[2])
      return h * 60 + min
    }

    //Formata o horário para HH:MM
    function formatHHMM(hhmm: string): string {
      const m = /^([01]?\d|2[0-3]):([0-5]\d)(?::([0-5]\d))?$/.exec(hhmm)
      if (!m) return hhmm
      const h = m[1].padStart(2, '0')
      const mi = m[2].padStart(2, '0')
      return `${h}:${mi}`
    }

    async function create() {
      if (!auth.isAdmin()) return alert('Somente ADMIN')
      error.value = null

      const start = parseMinutes(inicio.value)
      const end = parseMinutes(fim.value)
      if (start == null || end == null) {
        error.value = 'Informe horários válidos no formato HH:MM.'
        return
      }
      const diff = end - start
      if (diff <= 0) {
        error.value = 'O horário final deve ser maior que o horário inicial.'
        return
      }
      if (diff > 120) {
        error.value = 'Faixa inválida: a duração máxima permitida é de 2 horas.'
        return
      }

      // Impedir criação de faixas adicionais
      if (list.value.length >= 4) {
        error.value = 'Limite atingido: já existem 4 faixas cadastradas.'
        return
      }

      // Impedir duplicidade de faixas de horário
      const startMin = start
      const endMin = end
      const alreadyExists = list.value.some((f) => {
        const fmStart = parseMinutes(f.inicio)
        const fmEnd = parseMinutes(f.fim)
        return fmStart === startMin && fmEnd === endMin
      })
      if (alreadyExists) {
        error.value = 'Já existe uma faixa com este intervalo de horário.'
        return
      }

      await tiposApi.createFaixa({
        inicio: inicio.value,
        fim: fim.value,
        descricao: descricao.value || `${inicio.value}-${fim.value}`,
        capacidadeMaxima: capacidade.value || undefined,
      })
      list.value = await tiposApi.listFaixas()
      inicio.value = ''
      fim.value = ''
      descricao.value = ''
      capacidade.value = 120
      error.value = null
    }

    async function criarPadrao4Faixas() {
      if (!auth.isAdmin()) return alert('Somente ADMIN')
      error.value = null

      // Faixas padrão a serem criadas (2h)
      const base: Array<{ inicio: string; fim: string; descricao: string }> = [
        { inicio: '08:00', fim: '10:00', descricao: '08:00-10:00' },
        { inicio: '10:00', fim: '12:00', descricao: '10:00-12:00' },
        { inicio: '13:00', fim: '15:00', descricao: '13:00-15:00' },
        { inicio: '15:00', fim: '17:00', descricao: '15:00-17:00' },
      ]

      // Atualiza lista corrente
      list.value = await tiposApi.listFaixas()
      const defaultSet = new Set(base.map((b) => `${b.inicio}-${b.fim}`))
      const existingLabels = new Set(list.value.map((f) => `${f.inicio}-${f.fim}`))
      const hasNonDefault = list.value.some((f) => !defaultSet.has(`${f.inicio}-${f.fim}`))

      // Se houver faixas não padrão, não gerar (evita mistura/duplicação)
      if (hasNonDefault) {
        error.value =
          'Existem faixas não padrão cadastradas. Remova ou ajuste-as antes de gerar as 4 faixas padrão.'
        return
      }

      // Cria apenas as faltantes
      const missing = base.filter((b) => !existingLabels.has(`${b.inicio}-${b.fim}`))
      if (missing.length === 0) {
        alert('As 4 faixas padrão já estão cadastradas.')
        return
      }

      for (const f of missing) {
        try {
          await tiposApi.createFaixa({
            inicio: f.inicio,
            fim: f.fim,
            descricao: f.descricao,
            capacidadeMaxima: 120,
          })
        } catch (e) {
          console.error(e)
        }
      }
      list.value = await tiposApi.listFaixas()
    }

    async function remove(id: number) {
      if (!auth.isAdmin()) return alert('Somente ADMIN')
      if (!confirm('Confirmar remoção da faixa?')) return
      try {
        await tiposApi.deleteFaixa(id)
        list.value = await tiposApi.listFaixas()
      } catch (e) {
        console.error(e)
        error.value = 'Não foi possível remover a faixa.'
      }
    }

    return {
      inicio,
      fim,
      descricao,
      capacidade,
      list,
      create,
      criarPadrao4Faixas,
      error,
      remove,
      formatHHMM,
    }
  },
}
</script>

<style scoped>
.grid {
  display: grid;
  grid-template-columns: repeat(5, minmax(160px, 1fr));
  gap: 12px;
  margin-bottom: 16px;
}

.error-alert {
  background: #fdecea;
  color: #a4281f;
  border: 1px solid #f5c2c0;
  border-radius: 6px;
  padding: 10px 12px;
  margin: 10px 0 16px;
}

.warn-alert {
  background: #fff8e1;
  color: #7a5300;
  border: 1px solid #ffe08a;
  border-radius: 6px;
  padding: 10px 12px;
  margin: 10px 0 16px;
}
</style>
