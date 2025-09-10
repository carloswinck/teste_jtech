import { describe, it, expect, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import { createPinia, setActivePinia } from 'pinia'
import { createVuetify } from 'vuetify'
import TasklistCard from '../TasklistCard.vue'

describe('TasklistCard', () => {
  let vuetify: ReturnType<typeof createVuetify>

  beforeEach(() => {
    setActivePinia(createPinia())
    vuetify = createVuetify()
  })

  it('should render tasklist information correctly', () => {
    const tasklist = {
      id: '1',
      name: 'Test List',
      description: 'Test Description',
      color: '#1976d2',
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
      completedTasks: 3,
      pendingTasks: 2
    }

    const wrapper = mount(TasklistCard, {
      props: { 
        tasklist,
        completedTasks: 3,
        pendingTasks: 2
      },
      global: {
        plugins: [vuetify]
      }
    })

    expect(wrapper.text()).toContain('Test List')
    expect(wrapper.text()).toContain('Test Description')
    expect(wrapper.text()).toContain('3')
    expect(wrapper.text()).toContain('2')
  })

  it('should emit click event when card is clicked', async () => {
    const tasklist = {
      id: '1',
      name: 'Test List',
      description: 'Test Description',
      color: '#1976d2',
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z'
    }

    const wrapper = mount(TasklistCard, {
      props: { tasklist },
      global: {
        plugins: [vuetify]
      }
    })

    await wrapper.find('.tasklist-card').trigger('click')

    expect(wrapper.emitted('click')).toBeTruthy()
    expect(wrapper.emitted('click')?.[0]).toEqual([tasklist])
  })

  it('should have edit and delete buttons in the menu', () => {
    const tasklist = {
      id: '1',
      name: 'Test List',
      description: 'Test Description',
      color: '#1976d2',
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z'
    }

    const wrapper = mount(TasklistCard, {
      props: { tasklist },
      global: {
        plugins: [vuetify]
      }
    })

    // Verificar se os elementos existem no DOM
    expect(wrapper.html()).toContain('Editar')
    expect(wrapper.html()).toContain('Excluir')
  })
})
