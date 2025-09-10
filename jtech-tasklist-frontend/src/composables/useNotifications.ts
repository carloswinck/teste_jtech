import { ref } from 'vue'

export interface Notification {
  id: string
  type: 'success' | 'error' | 'warning' | 'info'
  title: string
  message: string
  duration?: number
}

export const useNotifications = () => {
  const notifications = ref<Notification[]>([])

  const addNotification = (notification: Omit<Notification, 'id'>) => {
    const id = Date.now().toString()
    const newNotification: Notification = {
      id,
      duration: 5000,
      ...notification
    }
    
    notifications.value.push(newNotification)
    
    // Auto remove after duration
    if (newNotification.duration) {
      setTimeout(() => {
        removeNotification(id)
      }, newNotification.duration)
    }
  }

  const removeNotification = (id: string) => {
    const index = notifications.value.findIndex(n => n.id === id)
    if (index > -1) {
      notifications.value.splice(index, 1)
    }
  }

  const clearAll = () => {
    notifications.value = []
  }

  // Convenience methods
  const success = (title: string, message: string) => {
    addNotification({ type: 'success', title, message })
  }

  const error = (title: string, message: string) => {
    addNotification({ type: 'error', title, message })
  }

  const warning = (title: string, message: string) => {
    addNotification({ type: 'warning', title, message })
  }

  const info = (title: string, message: string) => {
    addNotification({ type: 'info', title, message })
  }

  return {
    notifications,
    addNotification,
    removeNotification,
    clearAll,
    success,
    error,
    warning,
    info
  }
}
