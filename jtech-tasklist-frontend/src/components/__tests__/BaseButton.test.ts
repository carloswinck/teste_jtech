import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import BaseButton from '../BaseButton.vue'

describe('BaseButton', () => {
  it('should render button with text', () => {
    const wrapper = mount(BaseButton, {
      props: {
        text: 'Click me'
      }
    })

    expect(wrapper.text()).toContain('Click me')
  })

  it('should emit click event when clicked', async () => {
    const wrapper = mount(BaseButton, {
      props: {
        text: 'Click me'
      }
    })

    await wrapper.trigger('click')

    expect(wrapper.emitted('click')).toBeTruthy()
  })

  it('should apply variant classes', () => {
    const wrapper = mount(BaseButton, {
      props: {
        text: 'Click me',
        type: 'primary'
      }
    })

    expect(wrapper.classes()).toContain('btn-primary')
  })
})
