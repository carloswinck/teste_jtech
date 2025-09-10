import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useTasklistStore } from '../tasklist'

// Mock fetch
global.fetch = vi.fn()

describe('Tasklist Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
    vi.clearAllMocks()
  })

  it('should initialize with empty tasklists', () => {
    const tasklistStore = useTasklistStore()
    expect(tasklistStore.tasklists).toEqual([])
    expect(tasklistStore.loading).toBe(false)
  })

  it('should create a new tasklist locally', async () => {
    const tasklistStore = useTasklistStore()
    const newTasklist = {
      name: 'Test List',
      description: 'Test Description',
      color: '#1976d2'
    }

    // Mock API error to test local creation
    ;(fetch as any).mockResolvedValueOnce({
      ok: false,
      status: 500
    })

    const result = await tasklistStore.createTasklist(newTasklist)

    expect(result).toMatchObject(newTasklist)
    expect(result.id).toBeDefined()
    expect(result.createdAt).toBeDefined()
    expect(tasklistStore.tasklists).toHaveLength(1)
    expect(tasklistStore.tasklists[0]).toMatchObject(newTasklist)
  })

  it('should fetch tasklists from API', async () => {
    const tasklistStore = useTasklistStore()
    const mockTasklists = [
      { id: '1', name: 'List 1', description: 'Desc 1', color: '#1976d2' },
      { id: '2', name: 'List 2', description: 'Desc 2', color: '#388e3c' }
    ]

    ;(fetch as any).mockResolvedValueOnce({
      ok: true,
      json: () => Promise.resolve(mockTasklists)
    })

    await tasklistStore.fetchTasklists()

    expect(tasklistStore.tasklists).toEqual(mockTasklists)
  })
})