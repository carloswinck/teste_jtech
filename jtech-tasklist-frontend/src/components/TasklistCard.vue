<template>
  <v-card
    :class="cardClasses"
    :elevation="elevation"
    :color="cardColor"
    :variant="variant"
    @click="handleClick"
    v-bind="$attrs"
  >
    <!-- Header com cor personalizada -->
    <div class="tasklist-header" :style="{ backgroundColor: tasklist.color }">
      <div class="tasklist-header-content">
        <v-icon color="white" size="24" class="mr-2">
          mdi-clipboard-list
        </v-icon>
        <div class="tasklist-title">
          {{ tasklist.name }}
        </div>
      </div>
      
      <!-- Menu de ações -->
      <v-menu>
        <template v-slot:activator="{ props }">
          <v-btn
            v-bind="props"
            icon
            color="white"
            size="small"
            variant="text"
            @click.stop
          >
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </template>
        
        <v-list>
          <v-list-item @click="$emit('edit', tasklist)" data-testid="edit-button">
            <template v-slot:prepend>
              <v-icon>mdi-pencil</v-icon>
            </template>
            <v-list-item-title>Editar</v-list-item-title>
          </v-list-item>
          
          <v-list-item @click="$emit('delete', tasklist)" class="text-error" data-testid="delete-button">
            <template v-slot:prepend>
              <v-icon color="error">mdi-delete</v-icon>
            </template>
            <v-list-item-title>Excluir</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </div>

    <!-- Conteúdo do card -->
    <v-card-text class="tasklist-content">
      <div v-if="tasklist.description" class="tasklist-description">
        {{ tasklist.description }}
      </div>
      
      <div v-else class="tasklist-description placeholder">
        Sem descrição
      </div>
      
      <!-- Estatísticas -->
      <div class="tasklist-stats">
        <div class="stat-item">
          <v-icon size="16" color="primary">mdi-check-circle</v-icon>
          <span class="stat-text">{{ completedTasks }} concluídas</span>
        </div>
        <div class="stat-item">
          <v-icon size="16" color="warning">mdi-clock-outline</v-icon>
          <span class="stat-text">{{ pendingTasks }} pendentes</span>
        </div>
      </div>
    </v-card-text>

    <!-- Footer com data de criação -->
    <v-card-actions class="tasklist-footer">
      <div class="tasklist-date">
        <v-icon size="14" color="grey">mdi-calendar</v-icon>
        <span class="date-text">{{ formatDate(tasklist.createdAt) }}</span>
      </div>
      
      <v-spacer />
      
      <v-btn
        color="primary"
        variant="text"
        size="small"
        @click.stop="$emit('view', tasklist.id)"
      >
        <v-icon left>mdi-arrow-right</v-icon>
        Ver Lista
      </v-btn>
    </v-card-actions>

    <!-- Loading overlay -->
    <v-overlay
      v-if="loading"
      :model-value="loading"
      contained
      class="tasklist-loading"
    >
      <v-progress-circular
        indeterminate
        color="primary"
        size="40"
      />
    </v-overlay>
  </v-card>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Tasklist {
  id: string
  name: string
  description: string
  color: string
  createdAt: string
  updatedAt: string
}

interface Props {
  tasklist: Tasklist
  loading?: boolean
  elevation?: number
  variant?: 'elevated' | 'flat' | 'tonal' | 'outlined' | 'text' | 'plain'
  completedTasks?: number
  pendingTasks?: number
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  elevation: 2,
  variant: 'elevated',
  completedTasks: 0,
  pendingTasks: 0
})

const emit = defineEmits<{
  click: [tasklist: Tasklist]
  edit: [tasklist: Tasklist]
  delete: [tasklist: Tasklist]
  view: [id: string]
}>()

const cardClasses = computed(() => ({
  'tasklist-card': true,
  'tasklist-card--loading': props.loading,
  'tasklist-card--clickable': true
}))

const cardColor = computed(() => {
  // Se a cor da tasklist for muito clara, usar fundo branco
  const hex = props.tasklist.color.replace('#', '')
  const r = parseInt(hex.substr(0, 2), 16)
  const g = parseInt(hex.substr(2, 2), 16)
  const b = parseInt(hex.substr(4, 2), 16)
  const brightness = (r * 299 + g * 587 + b * 114) / 1000
  
  return brightness > 128 ? 'white' : 'grey-lighten-5'
})

const handleClick = () => {
  emit('click', props.tasklist)
}

const formatDate = (dateString: string) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric'
  })
}
</script>

<style scoped>
.tasklist-card {
  height: 100%;
  transition: all 0.3s ease;
  border-radius: 12px;
  overflow: hidden;
  position: relative;
}

.tasklist-card--clickable {
  cursor: pointer;
}

.tasklist-card--clickable:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15) !important;
}

.tasklist-card--loading {
  pointer-events: none;
}

.tasklist-header {
  padding: 16px;
  color: white;
  position: relative;
  min-height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.tasklist-header-content {
  display: flex;
  align-items: center;
  flex: 1;
}

.tasklist-title {
  font-size: 1.1rem;
  font-weight: 600;
  line-height: 1.2;
  word-break: break-word;
}

.tasklist-content {
  padding: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.tasklist-description {
  font-size: 0.9rem;
  color: #666;
  line-height: 1.4;
  min-height: 40px;
  display: flex;
  align-items: center;
}

.tasklist-description.placeholder {
  color: #ccc;
  font-style: italic;
}

.tasklist-stats {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.stat-text {
  font-size: 0.8rem;
  color: #666;
  font-weight: 500;
}

.tasklist-footer {
  padding: 16px 20px;
  border-top: 1px solid #f0f0f0;
  background-color: #fafafa;
}

.tasklist-date {
  display: flex;
  align-items: center;
  gap: 4px;
}

.date-text {
  font-size: 0.8rem;
  color: #666;
}

.tasklist-loading {
  background-color: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(2px);
}

/* Responsive */
@media (max-width: 600px) {
  .tasklist-content {
    padding: 16px;
  }
  
  .tasklist-footer {
    padding: 12px 16px;
  }
  
  .tasklist-stats {
    gap: 12px;
  }
  
  .stat-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 2px;
  }
}

/* Dark mode support */
@media (prefers-color-scheme: dark) {
  .tasklist-description {
    color: #ccc;
  }
  
  .tasklist-description.placeholder {
    color: #666;
  }
  
  .stat-text,
  .date-text {
    color: #ccc;
  }
  
  .tasklist-footer {
    background-color: #2a2a2a;
    border-top-color: #444;
  }
}
</style>
