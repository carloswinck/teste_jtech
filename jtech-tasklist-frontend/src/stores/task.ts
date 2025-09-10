import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export interface Task {
  id: string
  tasklistId: string
  title: string
  description: string
  completed: boolean
  priority: string
  dueDate: string
  createdAt: string
  updatedAt: string
}

export const useTaskStore = defineStore('task', () => {
  const tasks = ref<Task[]>([])
  const loading = ref(false)

  const getTasks = computed(() => tasks.value)
  const getTasksByTasklist = computed(() => (tasklistId: string) => 
    tasks.value.filter(task => task.tasklistId === tasklistId)
  )

  const fetchTasks = async () => {
    loading.value = true
    try {
      const response = await fetch('http://localhost:8080/api/v1/tasks')
      if (response.ok) {
        tasks.value = await response.json()
      }
    } catch (err) {
      console.error('Erro ao buscar tasks:', err)
    } finally {
      loading.value = false
    }
  }

  const fetchTasksByTasklist = async (tasklistId: string) => {
    loading.value = true
    try {
      const response = await fetch(`http://localhost:8080/api/v1/tasks/tasklist/${tasklistId}`)
      if (response.ok) {
        const tasklistTasks = await response.json()
        tasks.value = tasks.value.filter(task => task.tasklistId !== tasklistId)
        tasks.value.push(...tasklistTasks)
        saveToStorage()
      } else {
        loadFromStorage(tasklistId)
      }
    } catch {
      loadFromStorage(tasklistId)
    } finally {
      loading.value = false
    }
  }

  const saveToStorage = () => {
    localStorage.setItem('tasks', JSON.stringify(tasks.value))
  }

  const loadFromStorage = (tasklistId?: string) => {
    const stored = localStorage.getItem('tasks')
    if (stored) {
      try {
        const allTasks = JSON.parse(stored)
        
        // Corrigir tasks com tasklistId inválido
        const correctedTasks = allTasks.map((task: Task) => {
          if (task.tasklistId === '[object Object]' || typeof task.tasklistId !== 'string') {
            // Se tasklistId é inválido, tentar usar o primeiro tasklist disponível
            const tasklists = JSON.parse(localStorage.getItem('tasklists') || '[]')
            if (tasklists.length > 0) {
              const firstTasklist = tasklists[0]
              return { ...task, tasklistId: firstTasklist.id }
            } else {
              return task
            }
          }
          return task
        })
        
        if (tasklistId) {
          const tasklistTasks = correctedTasks.filter((task: Task) => task.tasklistId === tasklistId)
          // Remove tasks antigas desta tasklist e adiciona as novas
          tasks.value = tasks.value.filter(task => task.tasklistId !== tasklistId)
          tasks.value.push(...tasklistTasks)
        } else {
          tasks.value = correctedTasks
        }
      } catch (error) {
        console.error('Erro ao carregar tasks do localStorage:', error)
      }
    }
  }

  const createTask = async (task: Omit<Task, 'id' | 'createdAt' | 'updatedAt'>) => {
    loading.value = true
    try {
      const response = await fetch('http://localhost:8080/api/v1/tasks', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(task)
      })
      
      if (response.ok) {
        const newTask = await response.json()
        tasks.value.push(newTask)
        saveToStorage()
        return newTask
      } else {
        const localTask = {
          ...task,
          id: Date.now().toString(),
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString()
        }
        tasks.value.push(localTask)
        saveToStorage()
        return localTask
      }
    } catch {
      const localTask = {
        ...task,
        id: Date.now().toString(),
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
      tasks.value.push(localTask)
      saveToStorage()
      return localTask
    } finally {
      loading.value = false
    }
  }

  const updateTask = async (id: string, task: Partial<Task>) => {
    loading.value = true
    try {
      const response = await fetch(`http://localhost:8080/api/v1/tasks/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(task)
      })
      if (response.ok) {
        const updatedTask = await response.json()
        const index = tasks.value.findIndex(t => t.id === id)
        if (index !== -1) {
          tasks.value[index] = updatedTask
        }
        return updatedTask
      }
    } catch (err) {
      console.error('Erro ao atualizar task:', err)
    } finally {
      loading.value = false
    }
  }

  const toggleTask = async (id: string) => {
    loading.value = true
    try {
      const response = await fetch(`http://localhost:8080/api/v1/tasks/${id}/toggle`, {
        method: 'PATCH'
      })
      if (response.ok) {
        const updatedTask = await response.json()
        const index = tasks.value.findIndex(t => t.id === id)
        if (index !== -1) {
          tasks.value[index] = updatedTask
        }
        saveToStorage()
        return updatedTask
      } else {
        // Fallback: alternar localmente
        const index = tasks.value.findIndex(t => t.id === id)
        if (index !== -1) {
          tasks.value[index].completed = !tasks.value[index].completed
          tasks.value[index].updatedAt = new Date().toISOString()
        }
        saveToStorage()
      }
    } catch (err) {
      console.error('Erro ao alternar task:', err)
      // Fallback: alternar localmente
      const index = tasks.value.findIndex(t => t.id === id)
      if (index !== -1) {
        tasks.value[index].completed = !tasks.value[index].completed
        tasks.value[index].updatedAt = new Date().toISOString()
      }
      saveToStorage()
    } finally {
      loading.value = false
    }
  }

  const deleteTask = async (id: string) => {
    loading.value = true
    try {
      const response = await fetch(`http://localhost:8080/api/v1/tasks/${id}`, {
        method: 'DELETE'
      })
      if (response.ok) {
        tasks.value = tasks.value.filter(t => t.id !== id)
      }
    } catch (err) {
      console.error('Erro ao deletar task:', err)
    } finally {
      loading.value = false
    }
  }

  // Função para corrigir tasks existentes
  const fixExistingTasks = () => {
    const stored = localStorage.getItem('tasks')
    if (stored) {
      try {
        const allTasks = JSON.parse(stored)
        const hasInvalidTasks = allTasks.some((task: Task) => 
          task.tasklistId === '[object Object]' || typeof task.tasklistId !== 'string'
        )
        
        if (hasInvalidTasks) {
          const tasklists = JSON.parse(localStorage.getItem('tasklists') || '[]')
          
          if (tasklists.length > 0) {
            const correctedTasks = allTasks.map((task: Task) => {
              if (task.tasklistId === '[object Object]' || typeof task.tasklistId !== 'string') {
                // Usar o primeiro tasklist disponível
                const firstTasklist = tasklists[0]
                return { ...task, tasklistId: firstTasklist.id }
              }
              return task
            })
            
            tasks.value = correctedTasks
            saveToStorage()
          }
        }
      } catch (error) {
        console.error('Erro ao corrigir tasks:', error)
      }
    }
  }

  // Inicializar dados do localStorage
  loadFromStorage()
  
  // Corrigir tasks existentes se necessário
  fixExistingTasks()
  

  return {
    tasks,
    loading,
    getTasks,
    getTasksByTasklist,
    fetchTasks,
    fetchTasksByTasklist,
    createTask,
    updateTask,
    toggleTask,
    deleteTask
  }
})
