import { config } from '@vue/test-utils'
import { createPinia } from 'pinia'
import { createVuetify } from 'vuetify'
import { vi } from 'vitest'
import 'vuetify/styles'

// Mock localStorage
const localStorageMock = {
  getItem: vi.fn(),
  setItem: vi.fn(),
  removeItem: vi.fn(),
  clear: vi.fn(),
}
Object.defineProperty(window, 'localStorage', {
  value: localStorageMock
})

// Mock fetch
global.fetch = vi.fn()

// Setup Pinia
const pinia = createPinia()

// Setup Vuetify
const vuetify = createVuetify({
  theme: {
    defaultTheme: 'light'
  }
})

config.global.plugins = [pinia, vuetify]

// Mock Vue Router
vi.mock('vue-router', () => ({
  useRoute: () => ({
    params: { id: 'test-id' }
  }),
  useRouter: () => ({
    push: vi.fn(),
    replace: vi.fn()
  })
}))