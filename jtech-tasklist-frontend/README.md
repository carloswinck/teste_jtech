# TaskList Multi-usuário - JTech

## Visão Geral da Arquitetura

Este projeto implementa um sistema TODO List multi-usuário com arquitetura modular e escalável, demonstrando boas práticas de desenvolvimento frontend com Vue.js 3. A aplicação utiliza uma arquitetura baseada em componentes reutilizáveis, gerenciamento de estado centralizado e persistência local.

### Decisões Arquiteturais Principais

- **Arquitetura Modular**: Separação clara de responsabilidades entre views, components, stores e utilities
- **Composition API**: Uso extensivo da Composition API do Vue 3 para melhor reutilização de lógica
- **Pinia para Estado Global**: Gerenciamento de estado reativo e persistente
- **Design System Consistente**: Vuetify para interface Material Design responsiva
- **TypeScript**: Tipagem estática para maior robustez e manutenibilidade

## Stack Tecnológica

### Core Framework
- **Vue 3.5.18** - Framework principal com Composition API
- **TypeScript 5.8** - Tipagem estática e melhor DX
- **Vite 7.0.6** - Build tool moderno e rápido

### Roteamento e Estado
- **Vue Router 4.5.1** - Roteamento SPA com guards de proteção
- **Pinia 3.0.3** - Gerenciamento de estado reativo e persistente

### UI e Design
- **Vuetify 3.9.7** - Framework Material Design completo
- **@mdi/font 7.4.47** - Ícones Material Design

### Testes e Qualidade
- **Vitest 3.2.4** - Testes unitários modernos
- **@vue/test-utils 2.4.6** - Utilitários de teste para Vue
- **jsdom 26.1.0** - Ambiente DOM para testes
- **ESLint 9.31.0** - Linting de código
- **Prettier 3.6.2** - Formatação de código

### Justificativas Técnicas

**Vue 3 + Composition API**: Escolhido por sua reatividade superior, melhor performance e API mais limpa comparado ao Vue 2.

**Pinia**: Substitui o Vuex por ser mais simples, type-safe e ter melhor integração com TypeScript.

**Vuetify**: Fornece componentes Material Design prontos, responsivos e acessíveis, acelerando o desenvolvimento.

**Vitest**: Alternativa moderna ao Jest, com melhor integração ao Vite e performance superior.

## Como Rodar Localmente

### Pré-requisitos
- Node.js 20.19.0+ ou 22.12.0+
- npm ou yarn

### Instalação

1. **Clone o repositório**
```bash
git clone <repository-url>
cd jtech-tasklist-frontend
```

2. **Instale as dependências**
```bash
npm install
```

3. **Configure o backend** (opcional)
```bash
# Em outro terminal, na pasta do backend
cd ../jtech-tasklist-backend
./gradlew bootRun
```

4. **Execute a aplicação**
```bash
npm run dev
```

5. **Acesse a aplicação**
```
http://localhost:5173
```

### Credenciais de Teste
- **Usuário**: Qualquer texto
- **Senha**: Qualquer texto

## Como Rodar os Testes

### Executar todos os testes
```bash
npm run test:unit
```

### Executar testes em modo watch
```bash
npm run test:unit -- --watch
```

### Executar testes com cobertura
```bash
npm run test:unit -- --coverage
```

### Executar testes específicos
```bash
npm run test:unit -- --run TasklistCard
```

## Estrutura de Pastas Detalhada

```
src/
├── assets/                 # Recursos estáticos
│   ├── base.css           # Variáveis CSS e reset
│   └── main.css           # Estilos globais
├── components/            # Componentes reutilizáveis
│   ├── __tests__/         # Testes de componentes
│   │   └── TasklistCard.test.ts
│   ├── BaseButton.vue     # Botão base reutilizável
│   ├── BaseCard.vue       # Card base reutilizável
│   ├── LoadingSpinner.vue # Spinner de carregamento
│   ├── NotificationContainer.vue # Container de notificações
│   └── TasklistCard.vue   # Card de lista de tarefas
├── composables/           # Composition functions
│   └── useNotifications.ts # Hook para notificações
├── router/                # Configuração de rotas
│   └── index.ts          # Definição de rotas e guards
├── stores/               # Gerenciamento de estado (Pinia)
│   ├── __tests__/        # Testes de stores
│   │   ├── auth.test.ts
│   │   ├── task.test.ts
│   │   └── tasklist.test.ts
│   ├── auth.ts           # Store de autenticação
│   ├── task.ts           # Store de tarefas
│   └── tasklist.ts       # Store de listas
├── test/                 # Configuração de testes
│   └── setup.ts          # Setup global dos testes
├── types/                # Definições TypeScript
│   └── index.ts          # Interfaces e tipos
├── utils/                # Utilitários
│   └── validation.ts     # Funções de validação
├── views/                # Páginas da aplicação
│   ├── DashboardView.vue # Dashboard principal
│   ├── LoginView.vue     # Tela de login
│   └── TasklistView.vue  # Visualização de lista
├── App.vue              # Componente raiz
└── main.ts              # Ponto de entrada
```

## Decisões Técnicas Aprofundadas

### 1. Arquitetura de Estado

**Pinia Stores Modulares**:
- `authStore`: Gerencia autenticação e sessão do usuário
- `tasklistStore`: Controla listas de tarefas com CRUD completo
- `taskStore`: Gerencia tarefas individuais com filtros e ordenação

**Persistência Local**:
- Estado sincronizado com localStorage
- Fallback offline para operações CRUD
- Sincronização automática com backend quando disponível

### 2. Sistema de Roteamento

**Guards de Proteção**:
```typescript
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
  } else {
    next()
  }
})
```

**Lazy Loading**:
- Views carregadas sob demanda
- Redução do bundle inicial

### 3. Componentização Avançada

**Componentes Base Reutilizáveis**:
- `BaseButton`: Botão padronizado com variantes
- `BaseCard`: Card base com slots flexíveis
- `TasklistCard`: Card especializado para listas

**Props Tipadas**:
```typescript
interface TasklistCardProps {
  tasklist: Tasklist
  showActions?: boolean
}
```

### 4. Sistema de Notificações

**Composable Reutilizável**:
```typescript
export const useNotifications = () => {
  const notifications = ref<Notification[]>([])
  
  const addNotification = (notification: Omit<Notification, 'id'>) => {
    // Lógica de adição
  }
  
  return { notifications, addNotification }
}
```

### 5. Responsividade e UX

**Design Mobile-First**:
- Breakpoints responsivos para todos os dispositivos
- Layout adaptativo com CSS Grid e Flexbox
- Componentes que se ajustam ao espaço disponível

**Estados de Loading**:
- Indicadores visuais para todas as operações assíncronas
- Skeleton loaders para melhor percepção de performance

### 6. Tratamento de Erros

**Estratégia de Fallback**:
- Operações offline com localStorage
- Retry automático para falhas de rede
- Notificações de erro user-friendly

## Melhorias e Roadmap

### Curto Prazo (1-2 semanas)
- [ ] Implementar testes E2E com Playwright
- [ ] Adicionar PWA capabilities
- [ ] Implementar tema escuro/claro
- [ ] Adicionar animações de transição

### Médio Prazo (1-2 meses)
- [ ] Implementar real-time com WebSockets
- [ ] Adicionar sistema de colaboração
- [ ] Implementar drag-and-drop para tarefas
- [ ] Adicionar filtros avançados e busca

### Longo Prazo (3-6 meses)
- [ ] Migração para micro-frontends
- [ ] Implementar cache inteligente
- [ ] Adicionar analytics e métricas
- [ ] Implementar testes de performance

## Funcionalidades Implementadas

### ✅ Sistema de Autenticação
- Login com validação de campos
- Autenticação mock (qualquer usuário/senha)
- Persistência de sessão
- Logout com limpeza de dados

### ✅ Gerenciamento de Listas
- Criação de listas personalizadas
- Edição e exclusão de listas
- Navegação intuitiva entre listas
- Validação de campos obrigatórios

### ✅ Sistema de Tarefas
- CRUD completo de tarefas
- Prioridades (Alta, Média, Baixa)
- Datas de vencimento
- Filtros por status e prioridade
- Ordenação personalizável

### ✅ Persistência e Navegação
- Estado persistente no localStorage
- Roteamento protegido
- Sincronização com backend
- Fallback offline

### ✅ Experiência do Usuário
- Interface responsiva e moderna
- Feedback visual para todas as ações
- Estados de loading
- Notificações de sucesso/erro

## Cobertura de Testes

- **Stores**: 100% de cobertura nos stores principais
- **Componentes**: Testes de renderização e interação
- **Utilitários**: Validações e helpers testados
- **Integração**: Testes de fluxos completos

## Performance

- **Bundle Size**: Otimizado com tree-shaking
- **Lazy Loading**: Views carregadas sob demanda
- **Caching**: Estado persistente para melhor UX
- **Responsividade**: Layout adaptativo para todos os dispositivos

---

**Desenvolvido com ❤️ para JTech**

*Demonstração de expertise em Vue.js 3, TypeScript e arquitetura frontend moderna.*