<template>
  <v-app>
    <v-app-bar color="primary" dark elevation="0">
      <v-app-bar-title class="d-flex align-center">
        <v-icon class="mr-2">mdi-clipboard-list</v-icon>
        TaskList - Dashboard
      </v-app-bar-title>
      <v-spacer />
      <v-btn @click="logout" color="white" variant="text">
        <v-icon left>mdi-logout</v-icon>
        Sair
      </v-btn>
    </v-app-bar>

    <v-main class="dashboard-main">
      <v-container fluid class="pa-6">
        <!-- Header Section -->
        <div class="dashboard-header mb-8">
          <div class="d-flex justify-space-between align-center mb-4">
            <div>
              <h1 class="text-h3 font-weight-bold text-grey-darken-2 mb-2">
                Minhas Listas de Tarefas
              </h1>
              <p class="text-body-1 text-grey-darken-1">
                Organize suas tarefas em listas personalizadas
              </p>
            </div>
          </div>
          
          <!-- Create New List Card -->
          <v-card class="create-list-card" elevation="2">
            <v-card-title class="d-flex align-center pa-6">
              <v-icon color="primary" class="mr-3">mdi-plus-circle</v-icon>
              <span class="text-h6">Nova Lista</span>
            </v-card-title>
            <v-card-text class="pa-6 pt-0">
              <v-form @submit.prevent="createNewTasklist" class="create-form">
                <v-row>
                  <v-col cols="12" md="6">
                    <v-text-field
                      v-model="newTasklistName"
                      label="Nome da Lista"
                      placeholder="Ex: Trabalho, Estudos, Pessoal..."
                      required
                      variant="outlined"
                      :rules="[rules.required, rules.minLength(2)]"
                      counter="50"
                      maxlength="50"
                    />
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field
                      v-model="newTasklistDescription"
                      label="Descrição (opcional)"
                      placeholder="Descreva o propósito da lista..."
                      variant="outlined"
                      counter="200"
                      maxlength="200"
                    />
                  </v-col>
                </v-row>
                <v-row>
                  <v-col cols="12" class="d-flex justify-end">
                    <v-btn 
                      type="submit" 
                      color="primary" 
                      size="large"
                      :loading="loading"
                      :disabled="!newTasklistName.trim()"
                    >
                      <v-icon left>mdi-plus</v-icon>
                      Criar Lista
                    </v-btn>
                  </v-col>
                </v-row>
              </v-form>
            </v-card-text>
          </v-card>
        </div>

        <!-- Loading State -->
        <div v-if="loading && tasklists.length === 0" class="loading-container">
          <v-progress-circular 
            indeterminate 
            color="primary" 
            size="64"
            width="6"
          />
          <p class="text-h6 text-grey-darken-1 mt-4">Carregando suas listas...</p>
        </div>

        <!-- Empty State -->
        <div v-else-if="!loading && tasklists.length === 0" class="empty-state">
          <v-icon size="120" color="grey-lighten-1">mdi-clipboard-list-outline</v-icon>
          <h2 class="text-h5 text-grey-darken-1 mt-4">Nenhuma lista criada ainda</h2>
          <p class="text-body-1 text-grey-darken-1 mt-2">
            Crie sua primeira lista de tarefas para começar a organizar seu dia
          </p>
        </div>

        <!-- Tasklists Grid -->
        <div v-else class="tasklists-grid">
          <v-row>
            <v-col
              v-for="tasklist in tasklists"
              :key="tasklist.id"
              cols="12"
              sm="6"
              md="4"
              lg="3"
              xl="2"
            >
              <TasklistCard
                :tasklist="tasklist"
                :loading="loading"
                :completed-tasks="getCompletedTasksCount(tasklist.id)"
                :pending-tasks="getPendingTasksCount(tasklist.id)"
                @click="(tasklist) => goToTasklist(tasklist.id)"
                @edit="(tasklist) => editTasklist(tasklist.id)"
                @delete="(tasklist) => deleteTasklist(tasklist.id)"
                @view="goToTasklist"
              />
            </v-col>
          </v-row>
        </div>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { storeToRefs } from 'pinia'
import { useAuthStore } from '../stores/auth'
import { useTasklistStore } from '../stores/tasklist'
import { useTaskStore } from '../stores/task'
import { useValidation } from '../composables/useValidation'
import { useNotifications } from '../composables/useNotifications'
import TasklistCard from '../components/TasklistCard.vue'

const router = useRouter()
const authStore = useAuthStore()
const tasklistStore = useTasklistStore()
const taskStore = useTaskStore()
const { rules } = useValidation()
const { success, error } = useNotifications()

const { tasklists, loading } = storeToRefs(tasklistStore)
const { tasks } = storeToRefs(taskStore)

const newTasklistName = ref('')
const newTasklistDescription = ref('')

// Cores predefinidas para as listas
const predefinedColors = [
  '#1976d2', // Blue
  '#388e3c', // Green
  '#f57c00', // Orange
  '#d32f2f', // Red
  '#7b1fa2', // Purple
  '#00796b', // Teal
  '#5d4037', // Brown
  '#455a64', // Blue Grey
  '#e91e63', // Pink
  '#ff5722'  // Deep Orange
]

onMounted(async () => {
  await Promise.all([
    tasklistStore.fetchTasklists(),
    taskStore.fetchTasks()
  ])
})

const createNewTasklist = async () => {
  if (!newTasklistName.value.trim()) return
  
  try {
    
    // Selecionar cor aleatória
    const randomColor = predefinedColors[Math.floor(Math.random() * predefinedColors.length)]
    
    await tasklistStore.createTasklist({
      name: newTasklistName.value.trim(),
      description: newTasklistDescription.value.trim(),
      color: randomColor
    })
    
    const createdName = newTasklistName.value.trim()
    newTasklistName.value = ''
    newTasklistDescription.value = ''
    success('Lista criada!', `A lista "${createdName}" foi criada com sucesso.`)
    
    // Recarregar as listas após criar
    await tasklistStore.fetchTasklists()
  } catch (err) {
    console.error('Erro ao criar tasklist:', err)
    error('Erro ao criar lista', 'Não foi possível criar a lista. Tente novamente.')
  }
}

const editTasklist = async (tasklistId: string) => {
  const tasklist = tasklists.value.find(tl => tl.id === tasklistId)
  if (!tasklist) return
  
  const newName = prompt('Novo nome:', tasklist.name)
  if (newName && newName.trim() !== tasklist.name) {
    try {
      await tasklistStore.updateTasklist(tasklistId, {
        ...tasklist,
        name: newName.trim()
      })
      success('Lista atualizada!', `A lista foi renomeada para "${newName.trim()}".`)
    } catch (err) {
      console.error('Erro ao atualizar tasklist:', err)
      error('Erro ao atualizar lista', 'Não foi possível atualizar a lista. Tente novamente.')
    }
  }
}

const deleteTasklist = async (id: string) => {
  const tasklist = tasklists.value.find(tl => tl.id === id)
  if (!tasklist) return
  
  if (confirm(`Tem certeza que deseja excluir a lista "${tasklist.name}"? Esta ação não pode ser desfeita.`)) {
    try {
      await tasklistStore.deleteTasklist(id)
      success('Lista excluída!', `A lista "${tasklist.name}" foi excluída com sucesso.`)
    } catch (err) {
      console.error('Erro ao excluir tasklist:', err)
      error('Erro ao excluir lista', 'Não foi possível excluir a lista. Tente novamente.')
    }
  }
}

const goToTasklist = (id: string) => {
  router.push(`/tasklist/${id}`)
}


const logout = () => {
  authStore.logout()
  router.push('/login')
}

// Funções para contar tarefas
const getCompletedTasksCount = (tasklistId: string) => {
  return tasks.value.filter(task => 
    task.tasklistId === tasklistId && task.completed
  ).length
}

const getPendingTasksCount = (tasklistId: string) => {
  return tasks.value.filter(task => 
    task.tasklistId === tasklistId && !task.completed
  ).length
}
</script>

<style scoped>
.dashboard-main {
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
}

.dashboard-header {
  margin-bottom: 2rem;
}

.create-list-card {
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.create-list-card:hover {
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.create-form {
  width: 100%;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 2rem;
  text-align: center;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 2rem;
  text-align: center;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.tasklists-grid {
  margin-top: 2rem;
}

/* Responsive adjustments */
@media (max-width: 960px) {
  .dashboard-main .v-container {
    padding: 1rem;
  }
  
  .dashboard-header {
    margin-bottom: 1.5rem;
  }
  
  .create-list-card .v-card-title,
  .create-list-card .v-card-text {
    padding: 1rem;
  }
}

@media (max-width: 600px) {
  .dashboard-main .v-container {
    padding: 0.5rem;
  }
  
  .create-list-card .v-card-title {
    padding: 1rem 1rem 0 1rem;
  }
  
  .create-list-card .v-card-text {
    padding: 0 1rem 1rem 1rem;
  }
  
  .empty-state {
    padding: 2rem 1rem;
  }
  
  .empty-state .v-icon {
    font-size: 80px !important;
  }
}

/* Dark mode support */
@media (prefers-color-scheme: dark) {
  .dashboard-main {
    background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
  }
  
  .create-list-card,
  .empty-state {
    background: #2a2a2a;
    border-color: #444;
  }
}
</style>
