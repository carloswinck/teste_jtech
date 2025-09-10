<template>
  <div class="notification-container">
    <TransitionGroup name="notification" tag="div">
      <v-alert
        v-for="notification in notifications"
        :key="notification.id"
        :type="notification.type"
        :title="notification.title"
        variant="tonal"
        class="notification-item"
        closable
        @click:close="removeNotification(notification.id)"
      >
        <template v-slot:prepend>
          <v-icon :color="getIconColor(notification.type)">
            {{ getIcon(notification.type) }}
          </v-icon>
        </template>
        
        <div class="notification-content">
          <div class="notification-title">{{ notification.title }}</div>
          <div class="notification-message">{{ notification.message }}</div>
        </div>
        
        <template v-slot:append>
          <v-btn
            icon
            size="small"
            variant="text"
            @click="removeNotification(notification.id)"
          >
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </template>
      </v-alert>
    </TransitionGroup>
  </div>
</template>

<script setup lang="ts">
import { useNotifications } from '../composables/useNotifications'

const { notifications, removeNotification } = useNotifications()

const getIcon = (type: string) => {
  const icons: Record<string, string> = {
    success: 'mdi-check-circle',
    error: 'mdi-alert-circle',
    warning: 'mdi-alert',
    info: 'mdi-information'
  }
  return icons[type] || 'mdi-information'
}

const getIconColor = (type: string) => {
  const colors: Record<string, string> = {
    success: 'success',
    error: 'error',
    warning: 'warning',
    info: 'info'
  }
  return colors[type] || 'info'
}
</script>

<style scoped>
.notification-container {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 9999;
  max-width: 400px;
  width: 100%;
}

.notification-item {
  margin-bottom: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-radius: 8px;
}

.notification-content {
  flex: 1;
}

.notification-title {
  font-weight: 600;
  margin-bottom: 4px;
}

.notification-message {
  font-size: 0.9rem;
  opacity: 0.9;
}

/* Transitions */
.notification-enter-active,
.notification-leave-active {
  transition: all 0.3s ease;
}

.notification-enter-from {
  opacity: 0;
  transform: translateX(100%);
}

.notification-leave-to {
  opacity: 0;
  transform: translateX(100%);
}

.notification-move {
  transition: transform 0.3s ease;
}

/* Responsive */
@media (max-width: 600px) {
  .notification-container {
    top: 10px;
    right: 10px;
    left: 10px;
    max-width: none;
  }
}
</style>
