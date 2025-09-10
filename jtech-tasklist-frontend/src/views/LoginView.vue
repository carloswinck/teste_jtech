<template>
  <v-app>
    <v-main class="login-page">
      <div class="login-container">
        <div class="login-card">
          <div class="login-header">
            <div class="logo">
              <v-icon size="48" color="white">mdi-clipboard-list</v-icon>
            </div>
            <h1 class="title">TaskList</h1>
            <p class="subtitle">Organize suas tarefas de forma simples</p>
          </div>
          
          <div class="login-form">
            <v-form @submit.prevent="handleLogin">
              <div class="form-group">
                <v-text-field
                  v-model="username"
                  label="Usuário"
                  name="username"
                  prepend-icon="mdi-account"
                  type="text"
                  variant="outlined"
                  class="custom-field"
                />
              </div>
              
              <div class="form-group">
                <v-text-field
                  v-model="password"
                  label="Senha"
                  name="password"
                  prepend-icon="mdi-lock"
                  type="password"
                  variant="outlined"
                  class="custom-field"
                />
              </div>
              
              <v-btn
                color="primary"
                size="x-large"
                block
                @click="handleLogin"
                :loading="loading"
                :disabled="!username || !password"
                class="login-button"
              >
                <v-icon left>mdi-login</v-icon>
                Entrar
              </v-btn>
            </v-form>
            
            <div class="info-text">
              <v-icon color="primary" size="20">mdi-information</v-icon>
              <span>Digite qualquer usuário e senha para entrar</span>
            </div>
          </div>
        </div>
      </div>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')
const loading = ref(false)

// Removido as regras de validação para evitar erros visuais

const handleLogin = async () => {
  if (!username.value || !password.value) return
  
  loading.value = true
  try {
    const success = authStore.login(username.value, password.value)
    if (success) {
      router.push('/')
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-container {
  width: 100%;
  max-width: 450px;
}

.login-card {
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-header {
  background: linear-gradient(135deg, #1976d2 0%, #1565c0 100%);
  padding: 40px 30px;
  text-align: center;
  color: white;
}

.logo {
  margin-bottom: 20px;
}

.title {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0 0 10px 0;
  color: white;
}

.subtitle {
  font-size: 1.1rem;
  margin: 0;
  opacity: 0.9;
  color: white;
}

.login-form {
  padding: 40px 30px;
}

.form-group {
  margin-bottom: 25px;
}

.custom-field {
  font-size: 1.1rem;
}

.custom-field :deep(.v-field__input) {
  padding: 16px 14px;
  font-size: 1.1rem;
}

.custom-field :deep(.v-label) {
  font-size: 1.1rem;
  font-weight: 500;
}

.login-button {
  height: 56px;
  font-size: 1.2rem;
  font-weight: 600;
  text-transform: none;
  border-radius: 12px;
  margin-top: 20px;
  box-shadow: 0 4px 12px rgba(25, 118, 210, 0.3);
  transition: all 0.3s ease;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(25, 118, 210, 0.4);
}

.info-text {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 25px;
  padding: 15px;
  background: #f5f5f5;
  border-radius: 10px;
  color: #666;
  font-size: 0.95rem;
}

/* Responsividade */
@media (max-width: 600px) {
  .login-page {
    padding: 10px;
  }
  
  .login-header {
    padding: 30px 20px;
  }
  
  .login-form {
    padding: 30px 20px;
  }
  
  .title {
    font-size: 2rem;
  }
}
</style>
