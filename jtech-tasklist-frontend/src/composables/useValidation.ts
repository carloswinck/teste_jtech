export const useValidation = () => {
  const rules = {
    required: (value: unknown) => {
      if (typeof value === 'string') {
        return !!value.trim() || 'Campo obrigatório'
      }
      return !!value || 'Campo obrigatório'
    },
    
    minLength: (min: number) => (value: string) => {
      return value.length >= min || `Mínimo de ${min} caracteres`
    },
    
    maxLength: (max: number) => (value: string) => {
      return value.length <= max || `Máximo de ${max} caracteres`
    },
    
    email: (value: string) => {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      return emailRegex.test(value) || 'Email inválido'
    },
    
    url: (value: string) => {
      try {
        new URL(value)
        return true
      } catch {
        return 'URL inválida'
      }
    },
    
    date: (value: string) => {
      const date = new Date(value)
      return !isNaN(date.getTime()) || 'Data inválida'
    },
    
    futureDate: (value: string) => {
      const date = new Date(value)
      const now = new Date()
      return date > now || 'Data deve ser futura'
    },
    
    positiveNumber: (value: number) => {
      return value > 0 || 'Número deve ser positivo'
    },
    
    range: (min: number, max: number) => (value: number) => {
      return (value >= min && value <= max) || `Valor deve estar entre ${min} e ${max}`
    },
    
    custom: (validator: (value: unknown) => boolean | string) => (value: unknown) => {
      const result = validator(value)
      return result === true || result
    }
  }

  const validateForm = (form: Record<string, unknown>, validationRules: Record<string, unknown[]>) => {
    const errors: Record<string, string> = {}
    
    for (const [field, fieldRules] of Object.entries(validationRules)) {
      const value = form[field]
      
      for (const rule of fieldRules) {
        const result = (rule as (value: unknown) => boolean | string)(value)
        if (result !== true) {
          errors[field] = result as string
          break
        }
      }
    }
    
    return {
      isValid: Object.keys(errors).length === 0,
      errors
    }
  }

  const createAsyncValidator = (validator: (value: unknown) => Promise<boolean | string>) => {
    return async (value: unknown) => {
      try {
        const result = await validator(value)
        return result === true || result
      } catch {
        return 'Erro na validação'
      }
    }
  }

  return {
    rules,
    validateForm,
    createAsyncValidator
  }
}
