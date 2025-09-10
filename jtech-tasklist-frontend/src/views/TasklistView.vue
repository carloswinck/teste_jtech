<template>
  <v-app>
    <v-app-bar color="primary" dark elevation="2">
      <v-btn icon @click="$router.push('/')" color="white">
        <v-icon>mdi-arrow-left</v-icon>
      </v-btn>
      <v-app-bar-title class="text-h6">
        {{ currentTasklist?.name || 'Carregando...' }}
      </v-app-bar-title>
      <v-spacer />
      <v-btn icon @click="logout" color="white">
        <v-icon>mdi-logout</v-icon>
      </v-btn>
    </v-app-bar>

    <v-main class="tasklist-main">
      <div class="desktop-container">
        <!-- Header da Lista -->
        <div v-if="currentTasklist" class="header-section">
          <v-card class="elevation-3 pa-6" :color="currentTasklist.color || 'primary'" dark>
            <div class="d-flex align-center">
              <v-icon size="50" class="mr-4">mdi-clipboard-list</v-icon>
              <div>
                <h1 class="text-h3 mb-2">{{ currentTasklist.name }}</h1>
                <p v-if="currentTasklist.description" class="text-h6 mb-0 opacity-90">
                  {{ currentTasklist.description }}
                </p>
              </div>
            </div>
          </v-card>
        </div>

        <!-- Estatísticas -->
        <div class="stats-section">
          <div class="stats-grid">
            <v-card class="text-center pa-6" color="info" dark>
              <v-icon size="40" class="mb-3">mdi-format-list-checks</v-icon>
              <div class="text-h4">{{ totalTasks }}</div>
              <div class="text-h6">Total de Tarefas</div>
            </v-card>
            <v-card class="text-center pa-6" color="success" dark>
              <v-icon size="40" class="mb-3">mdi-check-circle</v-icon>
              <div class="text-h4">{{ completedTasks }}</div>
              <div class="text-h6">Concluídas</div>
            </v-card>
            <v-card class="text-center pa-6" color="warning" dark>
              <v-icon size="40" class="mb-3">mdi-clock-outline</v-icon>
              <div class="text-h4">{{ pendingTasks }}</div>
              <div class="text-h6">Pendentes</div>
            </v-card>
          </div>
        </div>

        <!-- Botão para criar nova task -->
        <div class="create-button-section">
          <v-btn
            color="primary"
            size="x-large"
            @click="showCreateDialog = true"
            prepend-icon="mdi-plus"
            class="elevation-3 create-task-btn"
            height="70"
            width="300"
          >
            <span class="text-h5">Nova Tarefa</span>
          </v-btn>
        </div>

        <!-- Filtros e Ordenação -->
        <div v-if="tasks.length > 0" class="filters-section">
          <div class="filters-grid">
            <v-text-field
              v-model="searchQuery"
              label="Buscar tarefas..."
              prepend-inner-icon="mdi-magnify"
              variant="outlined"
              clearable
              hide-details
              size="large"
              class="search-field"
            />
            <v-select
              v-model="filterStatus"
              :items="statusOptions"
              item-title="text"
              item-value="value"
              label="Filtrar por status"
              variant="outlined"
              hide-details
              size="large"
            />
            <v-select
              v-model="sortBy"
              :items="sortOptions"
              item-title="text"
              item-value="value"
              label="Ordenar por"
              variant="outlined"
              hide-details
              size="large"
            />
          </div>
        </div>

        <!-- Lista de tasks -->
        <div v-if="loading" class="loading-section">
          <div class="text-center">
            <v-progress-circular indeterminate color="primary" size="80" />
            <p class="text-h5 mt-6">Carregando tarefas...</p>
          </div>
        </div>

        <div v-else-if="filteredTasks.length === 0" class="empty-section">
          <v-card class="elevation-3">
            <v-card-text class="text-center pa-12">
              <v-icon size="120" color="grey-lighten-1">mdi-checkbox-marked-circle-outline</v-icon>
              <p class="text-h4 mt-6 mb-4">Nenhuma tarefa encontrada</p>
              <p class="text-h6 mb-6">
                {{ searchQuery ? 'Tente ajustar os filtros de busca' : 'Clique em "Nova Tarefa" para começar' }}
              </p>
              <v-btn
                v-if="!searchQuery"
                color="primary"
                size="large"
                @click="showCreateDialog = true"
                prepend-icon="mdi-plus"
              >
                Criar Primeira Tarefa
              </v-btn>
            </v-card-text>
          </v-card>
        </div>

        <div v-else class="tasks-section">
          <div class="tasks-grid">
            <v-card 
              v-for="task in filteredTasks"
              :key="task.id"
              class="task-card elevation-3" 
              :class="{ 
                'completed': task.completed,
                'task-card--completed': task.completed 
              }"
              :style="{ 
                borderLeft: `6px solid ${getPriorityColor(task.priority)}` 
              }"
            >
              <v-card-title class="d-flex align-center pa-6">
                <v-checkbox
                  v-model="task.completed"
                  @change="toggleTask(task.id)"
                  color="success"
                  hide-details
                  class="mr-3"
                  size="large"
                />
                <div class="flex-grow-1">
                  <h3 
                    class="text-h5 mb-2" 
                    :class="{ 
                      'text-decoration-line-through text-grey': task.completed 
                    }"
                  >
                    {{ task.title }}
                  </h3>
                  <div v-if="task.dueDate" class="text-body-1 text-grey">
                    <v-icon size="18" class="mr-2">mdi-calendar</v-icon>
                    {{ formatDate(task.dueDate) }}
                  </div>
                </div>
                <v-menu>
                  <template v-slot:activator="{ props }">
                    <v-btn
                      icon
                      size="large"
                      v-bind="props"
                      color="grey"
                    >
                      <v-icon>mdi-dots-vertical</v-icon>
                    </v-btn>
                  </template>
                  <v-list>
                    <v-list-item @click="editTask(task)">
                      <template v-slot:prepend>
                        <v-icon>mdi-pencil</v-icon>
                      </template>
                      <v-list-item-title>Editar</v-list-item-title>
                    </v-list-item>
                    <v-list-item @click="deleteTask(task.id)" class="text-error">
                      <template v-slot:prepend>
                        <v-icon color="error">mdi-delete</v-icon>
                      </template>
                      <v-list-item-title>Excluir</v-list-item-title>
                    </v-list-item>
                  </v-list>
                </v-menu>
              </v-card-title>
              
              <v-card-text v-if="task.description" class="pt-0 pa-6">
                <p class="text-body-1 text-grey-darken-1">{{ task.description }}</p>
              </v-card-text>
              
              <v-card-actions class="pa-6 pt-0">
                <v-chip
                  v-if="task.priority"
                  :color="getPriorityColor(task.priority)"
                  size="large"
                  variant="tonal"
                >
                  <v-icon start size="20">{{ getPriorityIcon(task.priority) }}</v-icon>
                  {{ getPriorityLabel(task.priority) }}
                </v-chip>
                <v-spacer />
                <v-chip
                  :color="task.completed ? 'success' : 'warning'"
                  size="large"
                  variant="tonal"
                >
                  <v-icon start size="20">
                    {{ task.completed ? 'mdi-check-circle' : 'mdi-clock-outline' }}
                  </v-icon>
                  {{ task.completed ? 'Concluída' : 'Pendente' }}
                </v-chip>
              </v-card-actions>
            </v-card>
          </div>
        </div>

        <!-- Dialog para criar/editar task -->
        <v-dialog v-model="showCreateDialog" max-width="800px">
          <v-card>
            <v-card-title class="pa-6">
              <span class="text-h4">{{ editingTask ? 'Editar Tarefa' : 'Nova Tarefa' }}</span>
            </v-card-title>
            <v-card-text class="pa-6">
              <v-form ref="form" v-model="valid">
                <v-text-field
                  v-model="taskForm.title"
                  label="Título da Tarefa"
                  :rules="[rules.required]"
                  variant="outlined"
                  size="large"
                  class="mb-6"
                />
                <v-textarea
                  v-model="taskForm.description"
                  label="Descrição (opcional)"
                  variant="outlined"
                  rows="4"
                  size="large"
                  class="mb-6"
                />
                <v-row>
                  <v-col cols="12" md="6">
                    <div class="priority-section">
                      <label class="priority-label">Prioridade</label>
                      <v-radio-group v-model="taskForm.priority" inline>
                        <v-radio
                          label="Baixa"
                          value="low"
                          color="success"
                          size="large"
                        />
                        <v-radio
                          label="Média"
                          value="medium"
                          color="warning"
                          size="large"
                        />
                        <v-radio
                          label="Alta"
                          value="high"
                          color="error"
                          size="large"
                        />
                      </v-radio-group>
                    </div>
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field
                      v-model="taskForm.dueDate"
                      label="Data de Vencimento"
                      type="date"
                      variant="outlined"
                      size="large"
                    />
                  </v-col>
                </v-row>
              </v-form>
            </v-card-text>
            <v-card-actions class="pa-6">
              <v-spacer />
              <v-btn @click="closeDialog" color="grey" size="large">
                Cancelar
              </v-btn>
              <v-btn 
                @click="saveTask" 
                color="primary"
                :disabled="!valid"
                :loading="loading"
                size="large"
              >
                {{ editingTask ? 'Atualizar' : 'Criar' }}
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </div>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { storeToRefs } from 'pinia'
import { useAuthStore } from '../stores/auth'
import { useTaskStore, type Task } from '../stores/task'
import { useTasklistStore } from '../stores/tasklist'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const taskStore = useTaskStore()
const tasklistStore = useTasklistStore()

const { tasks, loading } = storeToRefs(taskStore)
const { tasklists } = storeToRefs(tasklistStore)

// Estado local
const showCreateDialog = ref(false)
const editingTask = ref<Task | null>(null)
const valid = ref(false)
const searchQuery = ref('')
const filterStatus = ref('all')
const sortBy = ref('createdAt')

// Formulário da tarefa
const taskForm = ref({
  title: '',
  description: '',
  priority: 'medium',
  dueDate: ''
})

// Computed properties
const currentTasklist = computed(() => {
  const tasklistId = String(route.params.id)
  return tasklists.value.find(tl => tl.id === tasklistId)
})

const totalTasks = computed(() => {
  const tasklistId = String(route.params.id)
  return tasks.value.filter(t => t.tasklistId === tasklistId).length
})
const completedTasks = computed(() => {
  const tasklistId = String(route.params.id)
  return tasks.value.filter(t => t.tasklistId === tasklistId && t.completed).length
})
const pendingTasks = computed(() => {
  const tasklistId = String(route.params.id)
  return tasks.value.filter(t => t.tasklistId === tasklistId && !t.completed).length
})

const filteredTasks = computed(() => {
  const tasklistId = String(route.params.id)
  
  // Filtrar apenas as tarefas da tasklist atual
  let filtered = tasks.value.filter(task => task.tasklistId === tasklistId)

  // Filtro por busca
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(task => 
      task.title.toLowerCase().includes(query) ||
      task.description?.toLowerCase().includes(query)
    )
  }

  // Filtro por status
  if (filterStatus.value !== 'all') {
    filtered = filtered.filter(task => 
      filterStatus.value === 'completed' ? task.completed : !task.completed
    )
  }

  // Ordenação
  filtered.sort((a, b) => {
    switch (sortBy.value) {
      case 'title':
        return a.title.localeCompare(b.title)
      case 'priority':
        const priorityOrder: Record<string, number> = { high: 3, medium: 2, low: 1 }
        return (priorityOrder[b.priority] || 0) - (priorityOrder[a.priority] || 0)
      case 'dueDate':
        return new Date(a.dueDate || 0).getTime() - new Date(b.dueDate || 0).getTime()
      default:
        return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
    }
  })

  return filtered
})

// Opções para selects
const statusOptions = [
  { text: 'Todas', value: 'all' },
  { text: 'Pendentes', value: 'pending' },
  { text: 'Concluídas', value: 'completed' }
]

const sortOptions = [
  { text: 'Data de Criação', value: 'createdAt' },
  { text: 'Título', value: 'title' },
  { text: 'Prioridade', value: 'priority' },
  { text: 'Data de Vencimento', value: 'dueDate' }
]

// Regras de validação
const rules = {
  required: (value: string) => !!value || 'Campo obrigatório'
}

// Métodos
const loadTasks = async () => {
  const tasklistId = String(route.params.id)
  if (tasklistId && tasklistId !== 'undefined') {
    await taskStore.fetchTasksByTasklist(tasklistId)
  }
}

const saveTask = async () => {
  if (!valid.value) return

  const tasklistId = String(route.params.id)
  const taskData = {
    ...taskForm.value,
    tasklistId,
    completed: false,
    dueDate: taskForm.value.dueDate ? `${taskForm.value.dueDate}T00:00:00` : ''
  }

  if (editingTask.value) {
    await taskStore.updateTask(editingTask.value.id, taskData)
  } else {
    await taskStore.createTask(taskData)
  }

  closeDialog()
}

const editTask = (task: Task) => {
  editingTask.value = task
  taskForm.value = {
    title: task.title,
    description: task.description || '',
    priority: task.priority || 'medium',
    dueDate: task.dueDate ? task.dueDate.split('T')[0] : ''
  }
  showCreateDialog.value = true
}

const deleteTask = async (id: string) => {
  if (confirm('Tem certeza que deseja excluir esta tarefa?')) {
    await taskStore.deleteTask(id)
  }
}

const toggleTask = async (id: string) => {
  await taskStore.toggleTask(id)
}

const closeDialog = () => {
  showCreateDialog.value = false
  editingTask.value = null
  taskForm.value = {
    title: '',
    description: '',
    priority: 'medium',
    dueDate: ''
  }
}

const logout = () => {
  authStore.logout()
  router.push('/login')
}

// Utilitários
const getPriorityColor = (priority: string) => {
  const colors: Record<string, string> = {
    high: 'error',
    medium: 'warning',
    low: 'success'
  }
  return colors[priority] || 'grey'
}

const getPriorityLabel = (priority: string) => {
  const labels: Record<string, string> = {
    high: 'Alta',
    medium: 'Média',
    low: 'Baixa'
  }
  return labels[priority] || 'Média'
}

const getPriorityIcon = (priority: string) => {
  const icons: Record<string, string> = {
    high: 'mdi-arrow-up',
    medium: 'mdi-minus',
    low: 'mdi-arrow-down'
  }
  return icons[priority] || 'mdi-minus'
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('pt-BR')
}

// Lifecycle
onMounted(() => {
  loadTasks()
})

// Watchers
watch(() => route.params.id, () => {
  loadTasks()
})
</script>

<style scoped>
.tasklist-main {
  background-color: #f5f5f5;
  min-height: 100vh;
  width: 100%;
}

.desktop-container {
  width: 100%;
  max-width: none;
  padding: 2rem;
  margin: 0;
}

.header-section {
  margin-bottom: 2rem;
}

.stats-section {
  margin-bottom: 2rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.create-button-section {
  margin-bottom: 2rem;
  display: flex;
  justify-content: center;
}

.filters-section {
  margin-bottom: 2rem;
}

.filters-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.loading-section,
.empty-section {
  margin-bottom: 2rem;
}

.tasks-section {
  margin-bottom: 2rem;
}

.tasks-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 1.5rem;
}

.task-card {
  transition: all 0.3s ease;
  cursor: pointer;
  min-height: 280px;
}

.task-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.2) !important;
}

.task-card--completed {
  opacity: 0.7;
  background-color: #f8f9fa;
}

.completed {
  text-decoration: line-through;
}

.priority-section {
  padding: 12px 0;
}

.priority-label {
  display: block;
  font-size: 1rem;
  font-weight: 600;
  color: rgba(0, 0, 0, 0.7);
  margin-bottom: 12px;
}

.create-task-btn {
  border-radius: 12px;
  font-weight: 600;
}

.search-field {
  font-size: 1.1rem;
}

/* Responsividade para diferentes tamanhos de tela */
@media (min-width: 1200px) {
  .tasks-grid {
    grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  }
}

@media (min-width: 1600px) {
  .tasks-grid {
    grid-template-columns: repeat(auto-fill, minmax(450px, 1fr));
  }
}

@media (min-width: 1920px) {
  .tasks-grid {
    grid-template-columns: repeat(auto-fill, minmax(500px, 1fr));
  }
  
  .task-card {
    min-height: 320px;
  }
}

@media (min-width: 2560px) {
  .tasks-grid {
    grid-template-columns: repeat(auto-fill, minmax(550px, 1fr));
  }
  
  .task-card {
    min-height: 360px;
  }
}

/* Mobile responsivo */
@media (max-width: 768px) {
  .desktop-container {
    padding: 1rem;
  }
  
  .tasks-grid {
    grid-template-columns: 1fr;
  }
  
  .stats-grid,
  .filters-grid {
    grid-template-columns: 1fr;
  }
}
</style>