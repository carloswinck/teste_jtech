<template>
  <v-btn
    :class="buttonClasses"
    :color="color"
    :size="size"
    :variant="variant"
    :loading="loading"
    :disabled="disabled"
    :prepend-icon="prependIcon"
    :append-icon="appendIcon"
    :block="block"
    :rounded="rounded"
    v-bind="$attrs"
  >
    <slot>{{ props.text }}</slot>
  </v-btn>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  color?: string
  size?: 'x-small' | 'small' | 'default' | 'large' | 'x-large'
  variant?: 'elevated' | 'flat' | 'tonal' | 'outlined' | 'text' | 'plain'
  loading?: boolean
  disabled?: boolean
  prependIcon?: string
  appendIcon?: string
  block?: boolean
  rounded?: boolean | string
  type?: 'primary' | 'secondary' | 'success' | 'warning' | 'error' | 'info'
  text?: string
}

const props = withDefaults(defineProps<Props>(), {
  color: 'primary',
  size: 'default',
  variant: 'elevated',
  loading: false,
  disabled: false,
  block: false,
  rounded: false,
  type: 'primary',
  text: ''
})

const buttonClasses = computed(() => ({
  'base-button': true,
  [`btn-${props.type}`]: true,
  [`base-button--${props.type}`]: true
}))
</script>

<style scoped>
.base-button {
  transition: all 0.3s ease;
  font-weight: 500;
  text-transform: none;
  border-radius: 8px;
}

.base-button--primary {
  background: linear-gradient(135deg, #1976d2 0%, #1565c0 100%);
  color: white;
}

.base-button--secondary {
  background: linear-gradient(135deg, #757575 0%, #616161 100%);
  color: white;
}

.base-button--success {
  background: linear-gradient(135deg, #4caf50 0%, #388e3c 100%);
  color: white;
}

.base-button--warning {
  background: linear-gradient(135deg, #ff9800 0%, #f57c00 100%);
  color: white;
}

.base-button--error {
  background: linear-gradient(135deg, #f44336 0%, #d32f2f 100%);
  color: white;
}

.base-button--info {
  background: linear-gradient(135deg, #2196f3 0%, #1976d2 100%);
  color: white;
}

.base-button--small {
  padding: 8px 16px;
  font-size: 0.875rem;
}

.base-button--medium {
  padding: 12px 24px;
  font-size: 1rem;
}

.base-button--large {
  padding: 16px 32px;
  font-size: 1.125rem;
}

.base-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.base-button:active {
  transform: translateY(0);
}

.base-button:disabled {
  opacity: 0.6;
  transform: none;
  box-shadow: none;
}

/* Rounded variants */
.base-button--rounded {
  border-radius: 50px;
}

/* Block variant */
.base-button--block {
  width: 100%;
}

/* Loading state */
.base-button--loading {
  pointer-events: none;
}

/* Responsive */
@media (max-width: 600px) {
  .base-button--small {
    padding: 6px 12px;
    font-size: 0.8rem;
  }
  
  .base-button--medium {
    padding: 10px 20px;
    font-size: 0.9rem;
  }
  
  .base-button--large {
    padding: 14px 28px;
    font-size: 1rem;
  }
}
</style>
