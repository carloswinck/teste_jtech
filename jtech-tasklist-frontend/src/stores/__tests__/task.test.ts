import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useTaskStore } from '../task'

// Mock fetch
global.fetch = vi.fn()

describe('Task Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
    vi.clearAllMocks()
  })

  it('should initialize with empty tasks', () => {
    const taskStore = useTaskStore()
    expect(taskStore.tasks).toEqual([])
    expect(taskStore.loading).toBe(false)
  })

  it('should create a new task locally', async () => {
    const taskStore = useTaskStore()
    const newTask = {
      title: 'Test Task',
      description: 'Test Description',
      priority: 'medium',
      dueDate: '2024-12-31T00:00:00',
      tasklistId: '1',
      completed: false
    }

    // Mock API error to test local creation
    ;(fetch as any).mockRejectedValueOnce(new Error('API Error'))

    const result = await taskStore.createTask(newTask)

    expect(result).toMatchObject(newTask)
    expect(result.id).toBeDefined()
    expect(result.createdAt).toBeDefined()
    expect(taskStore.tasks).toHaveLength(1)
    expect(taskStore.tasks[0]).toMatchObject(newTask)
  })

  it('should filter tasks by tasklist', () => {
    const taskStore = useTaskStore()
    const tasks = [
      { 
        id: '1', 
        title: 'Task 1', 
        tasklistId: '1',
        description: '',
        completed: false,
        priority: 'medium',
        dueDate: '',
        createdAt: '',
        updatedAt: ''
      },
      { 
        id: '2', 
        title: 'Task 2', 
        tasklistId: '2',
        description: '',
        completed: false,
        priority: 'medium',
        dueDate: '',
        createdAt: '',
        updatedAt: ''
      },
      { 
        id: '3', 
        title: 'Task 3', 
        tasklistId: '1',
        description: '',
        completed: false,
        priority: 'medium',
        dueDate: '',
        createdAt: '',
        updatedAt: ''
      }
    ]

    taskStore.tasks = tasks

    const tasklist1Tasks = taskStore.getTasksByTasklist('1')
    expect(tasklist1Tasks).toHaveLength(2)
    expect(tasklist1Tasks.map(t => t.id)).toEqual(['1', '3'])
  })
})