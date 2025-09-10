import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useAuthStore } from '../auth'

describe('Auth Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })

  it('should initialize with no user', () => {
    const authStore = useAuthStore()
    expect(authStore.user).toBeNull()
    expect(authStore.isAuthenticated).toBe(false)
  })

  it('should login with valid credentials', () => {
    const authStore = useAuthStore()
    
    const result = authStore.login('testuser', 'password')
    
    expect(result).toBe(true)
    expect(authStore.user).toEqual({
      id: '1',
      username: 'testuser',
      email: 'testuser@example.com'
    })
    expect(authStore.isAuthenticated).toBe(true)
  })

  it('should fail login with invalid credentials', () => {
    const authStore = useAuthStore()
    
    const result = authStore.login('', '')
    
    expect(result).toBe(false)
    expect(authStore.user).toBeNull()
    expect(authStore.isAuthenticated).toBe(false)
  })

  it('should logout and clear user data', () => {
    const authStore = useAuthStore()
    
    authStore.login('testuser', 'password')
    expect(authStore.isAuthenticated).toBe(true)
    
    authStore.logout()
    
    expect(authStore.user).toBeNull()
    expect(authStore.isAuthenticated).toBe(false)
  })
})