import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export interface User {
  id: string
  username: string
  email: string
}

export const useAuthStore = defineStore('auth', () => {
  const user = ref<User | null>(null)
  const isAuthenticated = computed(() => !!user.value)

  const login = (username: string, password: string) => {
    // Simulação de autenticação conforme enunciado
    if (username && password) {
      user.value = {
        id: '1',
        username,
        email: `${username}@example.com`
      }
      return true
    }
    return false
  }

  const logout = () => {
    user.value = null
  }

  return {
    user,
    isAuthenticated,
    login,
    logout
  }
})
