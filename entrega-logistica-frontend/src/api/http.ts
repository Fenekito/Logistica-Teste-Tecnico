import axios from 'axios'
import { useAuthStore } from '../stores/auth'

const baseURL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api/v1'

const http = axios.create({
  baseURL,
  headers: { 'Content-Type': 'application/json' },
})

// adiciona Authorization automaticamente com o token JWT
http.interceptors.request.use((config) => {
  try {
    const auth = useAuthStore()
    const token = auth.token
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }
  } catch (e) {
    console.log(e)
  }
  return config
})

export default http
