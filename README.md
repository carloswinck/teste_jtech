# Desafio Técnico Fullstack2 - JTech

## Sistema TODO List Multi-usuário com Arquitetura Avançada

### Contextualização e Objetivo

A **JTech** busca desenvolvedores frontend experientes capazes de construir aplicações robustas e escaláveis com arquitetura bem definida. Este desafio avalia sua competência em gerenciamento de estado complexo, arquitetura modular e implementação de sistemas multi-usuário.

**Objetivo:** Desenvolver uma aplicação frontend sofisticada que simule um sistema TODO List multi-usuário, demonstrando expertise em arquitetura de componentes, gerenciamento de estado avançado e boas práticas de desenvolvimento.

## Especificações Técnicas

### Requisitos Funcionais

#### Sistema de Autenticação Simulada

1. **Interface de Login**: Tela de autenticação com validação de campos não vazios
2. **Autenticação Mock**: Qualquer combinação válida de usuário/senha redireciona para a aplicação
3. **Persistência de Sessão**: Manter dados do usuário logado no estado global da aplicação

#### Gerenciamento Avançado de Listas

1. **Múltiplas Listas de Tarefas**: Usuário pode criar listas categorizadas (ex: "Trabalho", "Estudos", "Pessoal")
2. **CRUD Completo de Listas**:
   * Criar novas listas com nomes personalizados
   * Renomear listas existentes com validação
   * Excluir listas com confirmação e verificação de dependências
3. **Navegação entre Listas**: Interface intuitiva para alternar entre diferentes listas

#### Sistema Completo de Tarefas

1. **Gerenciamento por Lista**: Cada lista mantém suas próprias tarefas independentemente
2. **CRUD de Tarefas**: Adicionar, editar, remover e marcar tarefas como concluídas dentro de cada lista
3. **Validações Avançadas**: Prevenção de duplicatas, validação de campos obrigatórios

#### Persistência e Navegação

1. **Estado Persistente**: Todo o estado (usuário, listas, tarefas) gerenciado pelo Pinia e persistido em `localStorage`
2. **Roteamento**: Vue Router para separar autenticação da aplicação principal
3. **Guards de Rota**: Proteção de rotas para usuários não autenticados

### Requisitos Não Funcionais

#### Arquitetura e Organização

1. **Estrutura Modular**: Projeto organizado com separação clara de responsabilidades (views, components, stores, utils)
2. **Componentização Avançada**: Componentes reutilizáveis com props tipadas e eventos bem definidos
3. **Composables**: Utilização de composition functions para lógicas reutilizáveis

#### Experiência do Usuário

1. **Design Responsivo Premium**: Interface adaptável e consistente em todos os dispositivos
2. **Feedback Interativo**: Notificações, loaders e animações para todas as ações do usuário
3. **Estados de Loading**: Simulação de operações assíncronas com indicadores visuais

### Stack Tecnológica Obrigatória

* **Framework**: Vue 3 (Composition API)
* **Roteamento**: Vue Router 4
* **Gerenciamento de Estado**: Pinia
* **UI Framework**: Material Design (Vuetify ou biblioteca equivalente)
* **Testes**: Vitest para testes unitários abrangentes
* **TypeScript**: Fortemente recomendado para tipagem robusta

## Critérios de Avaliação

* **Qualidade Arquitetural**: Código limpo, bem documentado e facilmente manutenível
* **Aplicação de Boas Práticas**: Demonstração de Clean Code, KISS e componentização eficaz
* **Arquitetura Modular**: Estrutura de projeto bem definida e organizada
* **Gerenciamento de Estado**: Uso correto e eficiente do Pinia para estado complexo
* **Qualidade dos Testes**: Cobertura abrangente testando componentes e stores
* **Domínio da Stack**: Utilização avançada das ferramentas do ecossistema Vue.js
* **Experiência do Usuário**: Interface intuitiva com excelente usabilidade

## Expectativa de Entrega

* **Prazo**: Até 3 dias corridos a partir do recebimento.
* **Formato**: Repositório Git com código-fonte completo e documentação detalhada.

### Estrutura Obrigatória do `README.md`

1. **Visão Geral da Arquitetura**: Descrição detalhada da estrutura e decisões arquiteturais
2. **Stack Tecnológica**: Lista completa com justificativas para cada escolha
3. **Como Rodar Localmente**: Instruções passo a passo para setup e execução
4. **Como Rodar os Testes**: Comandos para executar suite completa de testes
5. **Estrutura de Pastas Detalhada**: Mapeamento completo da organização modular do código
6. **Decisões Técnicas Aprofundadas**: Justificativas detalhadas sobre escolhas arquiteturais, padrões e bibliotecas
7. **Melhorias e Roadmap**: Propostas técnicas para evolução e escalabilidade da aplicação

---

**Boa sorte! A JTech espera uma solução que demonstre maturidade em desenvolvimento frontend e visão arquitetural.**
