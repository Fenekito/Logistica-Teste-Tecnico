<template>
  <div class="agendamento">
    <h2>Agendamentos</h2>

    <div v-if="faixas.length !== 4" class="warn">
      Atenção: Foram cadastradas {{ faixas.length }} faixas de horário. O recomendado é ter 4 faixas
      de 2 horas. Cadastre as faixas restantes na área de administração de faixas de horário.
    </div>

    <div class="capacity" v-if="ocupacaoDiaPct !== null || ocupacaoFaixaPct !== null">
      <div v-if="ocupacaoDiaPct !== null">
        Ocupação do dia: <strong>{{ ocupacaoDiaPct }}%</strong>
      </div>
      <div v-if="ocupacaoFaixaPct !== null">
        Ocupação da faixa: <strong>{{ ocupacaoFaixaPct }}%</strong>
      </div>
    </div>

    <form v-if="isAdmin" @submit.prevent="submit">
      <div>
        <label>Data</label>
        <input type="date" v-model="form.data" @change="loadDay" required />
      </div>

      <div>
        <label>Faixa de Horário</label>
        <select v-model="form.faixaId" required @change="recalcularOcupacao">
          <option v-for="f in faixas" :key="f.id" :value="f.id">
            {{ f.descricao ?? f.inicio + '-' + f.fim }}
          </option>
        </select>
        <small>Capacidade faixa: selecione uma das 4 faixas</small>
      </div>

      <div>
        <label>ID do Pedido</label>
        <input type="number" v-model.number="form.idPedido" required />
      </div>

      <div>
        <label>Fornecedor</label>
        <input v-model="form.fornecedor" maxlength="80" required />
      </div>

      <div>
        <label>Email do Fornecedor</label>
        <input type="email" v-model="form.emailFornecedor" maxlength="80" required />
      </div>

      <div>
        <label>Tipo Caminhão</label>
        <select v-model="form.tipoCaminhaoId" required>
          <option v-for="t in caminhaos" :key="t.id" :value="t.id">{{ t.nome }}</option>
        </select>
      </div>

      <div>
        <label>Tipo Paletização</label>
        <select v-model="form.tipoPaletizacaoId" required>
          <option v-for="p in paletizacoes" :key="p.id" :value="p.id">
            {{ prettyPaletizacao(p.nome) }}
          </option>
        </select>
      </div>

      <div>
        <label>Observação</label>
        <textarea v-model="form.observacao" maxlength="500"></textarea>
      </div>

      <button type="submit">Cadastrar</button>
    </form>

    <div v-if="lastCreated" class="result">
      <h3>Agendamento criado</h3>
      <p>
        Ocupação dia: {{ lastCreated.ocupacaoDiaPct }}% - Faixa: {{ lastCreated.ocupacaoFaixaPct }}%
      </p>
      <div v-if="lastCreated.excedeuDia || lastCreated.excedeuFaixa" class="alert">
        <strong>Aviso:</strong>
        <div v-if="lastCreated.excedeuDia">Excedeu capacidade do dia</div>
        <div v-if="lastCreated.excedeuFaixa">Excedeu capacidade da faixa</div>
      </div>
    </div>

    <hr />

    <h3>Agendamentos do dia</h3>
    <div>
      <input type="date" v-model="day" @change="loadDay" />
      <button @click="loadDay">Carregar</button>
    </div>

    <table v-if="agendamentos.length">
      <thead>
        <tr>
          <th>Pedido</th>
          <th>Fornecedor</th>
          <th>Faixa</th>
          <th>Caminhão</th>
          <th>Paletização</th>
          <th>Status</th>
          <th>Criado</th>
          <th v-if="isAdmin">Ações</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="a in agendamentos" :key="a.id">
          <td>{{ a.idPedido }}</td>
          <td>{{ a.fornecedor }}</td>
          <td>{{ a.faixaDescricao }}</td>
          <td>{{ a.tipoCaminhao }}</td>
          <td>{{ prettyPaletizacao(a.tipoPaletizacao) }}</td>
          <td>{{ a.status }}</td>
          <td>{{ formatDateTime(a.criadoEm) }}</td>
          <td v-if="isAdmin">
            <button @click="cancel(a.id)">Cancelar</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script lang="ts">
import { ref, onMounted, computed } from 'vue'
import { tiposApi, agendamentoApi, type AgendamentoListItem } from '../services/api'
import type { AxiosError } from 'axios'
import { useAuthStore } from '../stores/auth'

interface Faixa {
  id: number
  descricao?: string
  inicio: string
  fim: string
}

interface Paletizacao {
  id: number
  nome: string
}

interface Caminhao {
  id: number
  nome: string
}

interface LastCreated {
  ocupacaoDiaPct: number
  ocupacaoFaixaPct: number
  excedeuDia: boolean
  excedeuFaixa: boolean
  mailto?: string
}

export default {
  setup() {
    const form = ref({
      data: '',
      faixaId: null as number | null,
      idPedido: null as number | null,
      fornecedor: '',
      emailFornecedor: '',
      tipoCaminhaoId: null as number | null,
      tipoPaletizacaoId: null as number | null,
      observacao: '',
    })

    const faixas = ref<Faixa[]>([])
    const paletizacoes = ref<Paletizacao[]>([])
    const caminhaos = ref<Caminhao[]>([])
    const agendamentos = ref<AgendamentoListItem[]>([])
    const lastCreated = ref<LastCreated | null>(null)
    const day = ref<string>(new Date().toISOString().slice(0, 10))
    const auth = useAuthStore()
    const isAdmin = computed(() => auth.isAdmin())
    const ocupacaoDiaPct = ref<number | null>(null)
    const ocupacaoFaixaPct = ref<number | null>(null)

    onMounted(async () => {
      await loadOptions()
      await loadDay()
    })

    async function loadOptions() {
      faixas.value = await tiposApi.listFaixas()
      paletizacoes.value = await tiposApi.listPaletizacao()
      caminhaos.value = await tiposApi.listCaminhoes()
    }

    async function loadDay() {
      if (!day.value) return
      try {
        agendamentos.value = await agendamentoApi.listByDay(day.value)
        recalcularOcupacao()
      } catch (e: unknown) {
        console.error(e)
      }
    }
    //Formatação do enum de paletização
    function prettyPaletizacao(name: string) {
      if (!name) return name
      if (name.toUpperCase().includes('NAO')) return 'Não paletizado'
      if (name.toUpperCase().includes('PALET')) return 'Paletizado'
      return name
    }

    function formatDateTime(s: string) {
      return s ? new Date(s).toLocaleString() : ''
    }

    function calcularPallets(items: AgendamentoListItem[]) {
      // Não paletizado conta em dobro
      let total = 0
      for (const a of items) {
        const n = a.tipoPaletizacao?.toUpperCase?.() ?? ''
        const isNaoPalet = n.includes('NAO') || n.includes('NÃO')
        total += isNaoPalet ? 2 : 1
      }
      return total
    }

    function recalcularOcupacao() {
      // Capacidade por proposta: 480 pallets/dia e 120 por faixa
      const CAP_DIA = 480
      const CAP_FAIXA = 120

      const totalDia = calcularPallets(agendamentos.value)
      ocupacaoDiaPct.value = Math.min(100, Math.round((totalDia / CAP_DIA) * 100))

      // Faixa selecionada
      if (form.value.faixaId) {
        const sel = faixas.value.find((f) => f.id === form.value.faixaId)
        if (!sel) {
          ocupacaoFaixaPct.value = null
        } else {
          const label = sel.descricao ?? `${sel.inicio}-${sel.fim}`
          const daFaixa = agendamentos.value.filter((a) => a.faixaDescricao === label)
          const totalFaixa = calcularPallets(daFaixa)
          ocupacaoFaixaPct.value = Math.min(100, Math.round((totalFaixa / CAP_FAIXA) * 100))
        }
      } else {
        ocupacaoFaixaPct.value = null
      }
    }

    async function submit() {
      //Verificação se o usuário atual possui permissão de administrador
      if (!isAdmin.value) {
        alert('Somente ADMIN pode criar agendamentos')
        return
      }
      try {
        const payload = {
          data: form.value.data,
          faixaId: form.value.faixaId,
          idPedido: form.value.idPedido,
          fornecedor: form.value.fornecedor,
          emailFornecedor: form.value.emailFornecedor,
          tipoCaminhaoId: form.value.tipoCaminhaoId,
          tipoPaletizacaoId: form.value.tipoPaletizacaoId,
          observacao: form.value.observacao,
        }
        const res = await agendamentoApi.create(payload)
        lastCreated.value = res
        // abrir automaticamente o cliente de email
        if (res?.mailto) {
          window.location.href = res.mailto
        }
        // atualizar listagem do dia
        await loadDay()
      } catch (err: unknown) {
        const ax = err as AxiosError<{ message?: string }>
        alert(ax?.response?.data?.message ?? 'Erro ao criar agendamento')
      }
    }

    async function cancel(id: number) {
      if (!isAdmin.value) {
        alert('Somente ADMIN pode cancelar agendamentos')
        return
      }
      if (!confirm('Confirmar cancelamento?')) return
      try {
        await agendamentoApi.cancel(id)
        await loadDay()
      } catch (e: unknown) {
        console.error(e)
        const ax = e as AxiosError<{ message?: string }>
        alert(ax?.response?.data?.message ?? 'Erro ao cancelar')
      }
    }

    return {
      form,
      faixas,
      paletizacoes,
      caminhaos,
      agendamentos,
      submit,
      prettyPaletizacao,
      formatDateTime,
      loadDay,
      day,
      lastCreated,
      cancel,
      isAdmin,
      ocupacaoDiaPct,
      ocupacaoFaixaPct,
      recalcularOcupacao,
    }
  },
}
</script>

<style scoped>
.alert {
  color: darkorange;
}
.warn {
  background: #fff8e1;
  color: #7a5300;
  border: 1px solid #ffe08a;
  border-radius: 6px;
  padding: 10px 12px;
  margin: 10px 0 16px;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 12px;
}
table th,
table td {
  border: 1px solid #ddd;
  padding: 6px;
}

.capacity {
  display: flex;
  gap: 16px;
  margin-bottom: 12px;
}
</style>
