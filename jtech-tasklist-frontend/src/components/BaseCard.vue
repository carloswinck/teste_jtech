<template>
  <v-card
    :class="cardClasses"
    :elevation="elevation"
    :color="color"
    :variant="variant"
    v-bind="$attrs"
  >
    <v-card-title v-if="title || $slots.title" class="card-title">
      <slot name="title">
        <div class="d-flex align-center">
          <v-icon v-if="icon" :color="iconColor" class="mr-2">
            {{ icon }}
          </v-icon>
          <span>{{ title }}</span>
        </div>
      </slot>
    </v-card-title>

    <v-card-subtitle v-if="subtitle || $slots.subtitle" class="card-subtitle">
      <slot name="subtitle">
        {{ subtitle }}
      </slot>
    </v-card-subtitle>

    <v-card-text v-if="$slots.default" class="card-content">
      <slot />
    </v-card-text>

    <v-card-actions v-if="$slots.actions" class="card-actions">
      <slot name="actions" />
    </v-card-actions>
  </v-card>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  title?: string
  subtitle?: string
  icon?: string
  iconColor?: string
  elevation?: number
  color?: string
  variant?: 'elevated' | 'flat' | 'tonal' | 'outlined' | 'text' | 'plain'
  hover?: boolean
  clickable?: boolean
  loading?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  elevation: 2,
  variant: 'elevated',
  hover: false,
  clickable: false,
  loading: false
})

const cardClasses = computed(() => ({
  'base-card': true,
  'base-card--hover': props.hover,
  'base-card--clickable': props.clickable,
  'base-card--loading': props.loading
}))
</script>

<style scoped>
.base-card {
  transition: all 0.3s ease;
  border-radius: 12px;
  overflow: hidden;
}

.base-card--hover:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15) !important;
}

.base-card--clickable {
  cursor: pointer;
}

.base-card--clickable:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15) !important;
}

.base-card--loading {
  opacity: 0.7;
  pointer-events: none;
}

.card-title {
  padding: 20px 20px 0 20px;
  font-weight: 600;
}

.card-subtitle {
  padding: 8px 20px 0 20px;
  opacity: 0.8;
}

.card-content {
  padding: 20px;
}

.card-actions {
  padding: 16px 20px 20px 20px;
  gap: 8px;
}

/* Responsive */
@media (max-width: 600px) {
  .card-title,
  .card-subtitle,
  .card-content,
  .card-actions {
    padding-left: 16px;
    padding-right: 16px;
  }
  
  .card-title {
    padding-top: 16px;
  }
  
  .card-actions {
    padding-bottom: 16px;
  }
}
</style>
