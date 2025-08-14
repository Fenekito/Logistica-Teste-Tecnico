import http from '../api/http'

export interface LoginRequest {
  email: string
  password: string
}
export interface LoginResponse {
  token: string
  bearer: string
}

export const authApi = {
  login: (payload: LoginRequest) =>
    http.post<LoginResponse>('/auth/login', payload).then((r) => r.data),
}

// resources (faixas, tipos, caminhões, controle-dia)
export const tiposApi = {
  listCaminhoes: () =>
    http.get<Array<{ id: number; nome: string }>>('/tipos-caminhao').then((r) => r.data),
  createCaminhao: (body: { nome: string }) =>
    http.post('/tipos-caminhao', body).then((r) => r.data),
  listPaletizacao: () =>
    http.get<Array<{ id: number; nome: string }>>('/tipos-paletizacao').then((r) => r.data),
  listFaixas: () =>
    http
      .get<
        Array<{ id: number; inicio: string; fim: string; descricao?: string }>
      >('/faixas-horario')
      .then((r) => r.data),
  createFaixa: (body: {
    inicio: string
    fim: string
    capacidadeMaxima?: number
    descricao?: string
  }) => http.post('/faixas-horario', body).then((r) => r.data),
  deleteFaixa: (id: number) => http.delete(`/faixas-horario/${id}`).then((r) => r.data),
  createControleDia: (body: { data: string; capacidadeMaxima?: number }) =>
    http.post('/controle-dia', body).then((r) => r.data),
}

export const agendamentoApi = {
  create: (payload: AgendamentoCreatePayload) =>
    http.post<AgendamentoCreateResponse>('/agendamentos', payload).then((r) => r.data),
  listByPeriod: (inicio: string, fim: string) =>
    http
      .get<
        AgendamentoListItem[]
      >(`/agendamentos?inicio=${encodeURIComponent(inicio)}&fim=${encodeURIComponent(fim)}`)
      .then((r) => r.data),
  listByDay: (data: string) =>
    http
      .get<AgendamentoListItem[]>(`/agendamentos/dia/${encodeURIComponent(data)}`)
      .then((r) => r.data),
  cancel: (id: number) => http.delete(`/agendamentos/${id}`).then((r) => r.data),
}

// Types
export interface AgendamentoCreatePayload {
  data: string
  faixaId: number | null
  idPedido: number | null
  fornecedor: string
  emailFornecedor: string
  tipoCaminhaoId: number | null
  tipoPaletizacaoId: number | null
  observacao?: string
}

export interface AgendamentoCreateResponse {
  ocupacaoDiaPct: number
  ocupacaoFaixaPct: number
  excedeuDia: boolean
  excedeuFaixa: boolean
  mailto?: string
}

export interface AgendamentoListItem {
  id: number
  idPedido: number
  fornecedor: string
  faixaDescricao: string
  tipoCaminhao: string
  tipoPaletizacao: string
  status: string
  criadoEm: string
}
