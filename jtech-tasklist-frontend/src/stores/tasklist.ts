import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export interface Tasklist {
  id: string
  name: string
  description: string
  color: string
  createdAt: string
  updatedAt: string
}

export const useTasklistStore = defineStore('tasklist', () => {
  const tasklists = ref<Tasklist[]>([])
  const currentTasklist = ref<Tasklist | null>(null)
  const loading = ref(false)

  const getTasklists = computed(() => tasklists.value)

  const fetchTasklists = async () => {
    loading.value = true
    try {
      const response = await fetch('http://localhost:8080/api/v1/tasklists')
      if (response.ok) {
        const data = await response.json()
        tasklists.value = data
        saveToStorage()
      } else {
        console.error('Erro na resposta:', response.status, response.statusText)
        // Fallback para dados do localStorage
        loadFromStorage()
      }
    } catch (error) {
      console.error('Erro ao buscar tasklists:', error)
      // Fallback para dados do localStorage
      loadFromStorage()
    } finally {
      loading.value = false
    }
  }

  const saveToStorage = () => {
    localStorage.setItem('tasklists', JSON.stringify(tasklists.value))
  }

  const loadFromStorage = () => {
    const stored = localStorage.getItem('tasklists')
    if (stored) {
      try {
        tasklists.value = JSON.parse(stored)
      } catch (error) {
        console.error('Erro ao carregar dados do localStorage:', error)
      }
    }
  }

  const createTasklist = async (tasklist: Omit<Tasklist, 'id' | 'createdAt' | 'updatedAt'>) => {
    loading.value = true
    try {
      const response = await fetch('http://localhost:8080/api/v1/tasklists', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(tasklist)
      })
      if (response.ok) {
        const newTasklist = await response.json()
        tasklists.value.push(newTasklist)
        saveToStorage()
        return newTasklist
      } else {
        console.error('Erro na criação:', response.status, response.statusText)
        // Fallback: criar localmente
        const localTasklist = {
          ...tasklist,
          id: Date.now().toString(),
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        }
        tasklists.value.push(localTasklist)
        saveToStorage()
        return localTasklist
      }
    } catch (error) {
      console.error('Erro ao criar tasklist:', error)
      // Fallback: criar localmente
      const localTasklist = {
        ...tasklist,
        id: Date.now().toString(),
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
      tasklists.value.push(localTasklist)
      saveToStorage()
      return localTasklist
    } finally {
      loading.value = false
    }
  }

  const updateTasklist = async (id: string, tasklist: Partial<Tasklist>) => {
    loading.value = true
    try {
      const response = await fetch(`http://localhost:8080/api/v1/tasklists/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(tasklist)
      })
      if (response.ok) {
        const updatedTasklist = await response.json()
        const index = tasklists.value.findIndex(t => t.id === id)
        if (index !== -1) {
          tasklists.value[index] = updatedTasklist
        }
        return updatedTasklist
      }
    } catch (error) {
      console.error('Erro ao atualizar tasklist:', error)
    } finally {
      loading.value = false
    }
  }

  const deleteTasklist = async (id: string) => {
    loading.value = true
    try {
      const response = await fetch(`http://localhost:8080/api/v1/tasklists/${id}`, {
        method: 'DELETE'
      })
      if (response.ok) {
        tasklists.value = tasklists.value.filter(t => t.id !== id)
        if (currentTasklist.value?.id === id) {
          currentTasklist.value = null
        }
      }
    } catch (error) {
      console.error('Erro ao deletar tasklist:', error)
    } finally {
      loading.value = false
    }
  }

  const setCurrentTasklist = (tasklist: Tasklist | null) => {
    currentTasklist.value = tasklist
  }

  // Inicializar dados do localStorage
  loadFromStorage()

  return {
    tasklists,
    currentTasklist,
    loading,
    getTasklists,
    fetchTasklists,
    createTasklist,
    updateTasklist,
    deleteTasklist,
    setCurrentTasklist
  }
})
