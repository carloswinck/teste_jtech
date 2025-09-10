<template>
  <div class="loading-container" :class="{ 'loading-overlay': overlay }">
    <div class="loading-content">
      <v-progress-circular
        :size="size"
        :width="width"
        :color="color"
        indeterminate
        class="loading-spinner"
      />
      <div v-if="message" class="loading-message">
        {{ message }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
interface Props {
  size?: number | string
  width?: number | string
  color?: string
  message?: string
  overlay?: boolean
}

withDefaults(defineProps<Props>(), {
  size: 40,
  width: 4,
  color: 'primary',
  message: '',
  overlay: false
})
</script>

<style scoped>
.loading-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(2px);
  z-index: 9999;
}

.loading-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.loading-spinner {
  animation: spin 1s linear infinite;
}

.loading-message {
  font-size: 1rem;
  color: #666;
  text-align: center;
  max-width: 300px;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* Dark mode support */
@media (prefers-color-scheme: dark) {
  .loading-overlay {
    background-color: rgba(0, 0, 0, 0.8);
  }
  
  .loading-message {
    color: #ccc;
  }
}
</style>
