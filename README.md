# 🌿 Florestae

> Monitoramento ambiental do Brasil em tempo real.

Aplicação Flutter desenvolvida para o monitoramento de desmatamento e emissões de carbono no Brasil. Através de dados mockados de satélites e sensores ambientais, o app permite visualizar áreas críticas e acompanhar as principais fontes de emissão de CO₂ por categoria e bioma.

**Desenvolvido por:** RM 557645 • RM 554540  
**Instituição:** FIAP — Global Solution 2026  
**Versão:** 1.0.0

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Fluxo de Telas](#-fluxo-de-telas)
- [Como Executar](#-como-executar)

---

## 🌱 Sobre o Projeto

O **Florestae** é uma solução mobile/web voltada ao monitoramento ambiental brasileiro. A aplicação exibe áreas desmatadas filtradas por bioma (Amazônia, Cerrado, Mata Atlântica, Pantanal, Caatinga), apresenta dados de emissões de CO₂ por categoria e oferece detalhes por região monitorada, com status de criticidade (Crítico, Alerta, Moderado, Estável).

---

## ✨ Funcionalidades

- **Onboarding** com apresentação guiada das principais funcionalidades
- **Monitoramento de áreas desmatadas** com status de criticidade
- **Filtro por bioma** (Todos, Amazônia, Cerrado, Mata Atlântica, Pantanal, Caatinga)
- **Detalhes por área** com hectares desmatados e emissões de CO₂
- **Rastreamento de emissões de CO₂** por categoria (Uso do Solo, Agricultura, Transporte, Energia, Indústria, Resíduos)
- **Tela "Sobre"** com informações do projeto e dos desenvolvedores

---

## 🛠 Tecnologias

| Tecnologia | Versão |
|---|---|
| Flutter | SDK ^3.11.5 |
| Dart | ^3.11.5 |
| Material Design | 3 |
| cupertino_icons | ^1.0.8 |

> Sem dependências externas além do próprio SDK Flutter — dados são mockados via repositories locais.

---

## 📁 Estrutura do Projeto

```
lib/
├── main.dart                        # Ponto de entrada, configuração do tema
├── model/
│   ├── monitored_area.dart          # Modelo de área monitorada
│   └── carbon_emission.dart         # Modelo de emissão de carbono
├── navigation/
│   ├── app_routes.dart              # Definição de rotas nomeadas
│   └── app_navigation.dart          # Geração de rotas e passagem de argumentos
├── repository/
│   ├── monitored_area_repository.dart    # Dados mockados de áreas
│   └── carbon_emission_repository.dart   # Dados mockados de emissões
└── ui/
    ├── components/
    │   ├── app_logo.dart            # Logo da aplicação
    │   ├── area_card.dart           # Card de área monitorada
    │   ├── emission_card.dart       # Card de emissão de carbono
    │   ├── eco_top_app_bar.dart     # AppBar principal
    │   └── eco_detail_top_app_bar.dart  # AppBar de detalhe
    └── screens/
        ├── splash_screen.dart       # Tela de splash
        ├── intro_screen.dart        # Telas de onboarding
        ├── home_screen.dart         # Tela principal (lista de áreas)
        ├── area_detail_screen.dart  # Detalhe de área monitorada
        ├── emissions_screen.dart    # Lista de emissões de carbono
        └── about_screen.dart        # Sobre o projeto
```

---

## 📱 Fluxo de Telas

O fluxo da aplicação segue a sequência abaixo:

```
Splash → Onboarding (4 slides) → Home → Detalhe da Área
                                      → Emissões de Carbono
                                      → Sobre o Florestae
```

---

### Tela 1 — Splash Screen

Tela de boas-vindas exibida na inicialização do app. Apresenta a logo do Florestae com o slogan "Monitoramento ambiental do Brasil em tempo real." e avança automaticamente para o onboarding.

> **Rota:** `/` (splash)

![Tela 1 - Splash](./screenshots/tela1_splash.png)

---

### Tela 2 — Onboarding: Bem-vindo ao Florestae

Primeiro slide do onboarding com a ilustração do mapa da América do Sul e satélite. Introduz o conceito do app ao usuário. Possui botão **Próximo** para avançar.

> **Rota:** `/intro` — Slide 1/4

![Tela 2 - Onboarding 1](./screenshots/tela2_onboarding_1.png)

---

### Tela 3 — Onboarding: Monitoramento por Satélite

Segundo slide do onboarding. Apresenta o ícone de satélite e destaca que as áreas de desmatamento são atualizadas via satélite. Possui botões **Voltar** e **Próximo**.

> **Rota:** `/intro` — Slide 2/4

![Tela 3 - Onboarding 2](./screenshots/tela3_onboarding_2.png)

---

### Tela 4 — Onboarding: Emissões de Carbono

Terceiro slide do onboarding. Apresenta o ícone de CO₂ e informa que o app permite acompanhar as fontes de emissão por categoria. Possui botões **Voltar** e **Próximo**.

> **Rota:** `/intro` — Slide 3/4

![Tela 4 - Onboarding 3](./screenshots/tela4_onboarding_3.png)

---

### Tela 5 — Onboarding: Dados por Bioma

Quarto e último slide do onboarding. Apresenta o ícone de gráfico de barras e informa que é possível filtrar por Amazônia, Cerrado, Mata Atlântica e mais. Botão **Começar** navega para a tela principal.

> **Rota:** `/intro` — Slide 4/4

![Tela 5 - Onboarding 4](./screenshots/tela5_onboarding_4.png)

---

### Tela 6 — Home: Áreas Monitoradas

Tela principal do app. Exibe a lista de áreas monitoradas com chips de filtro por bioma no topo. Cada card mostra nome, bioma, hectares desmatados, emissões de CO₂ e badge de status (Crítico, Alerta, Moderado, Estável). No canto superior direito há atalho para **Emissões de Carbono** e botão de **Informações**.

> **Rota:** `/home`

| Campo | Descrição |
|---|---|
| Nome | Identificação da região (ex: Pará - Região Sul) |
| Bioma | Bioma ao qual pertence (ex: Amazônia) |
| Hectares | Área desmatada em ha |
| CO₂ | Emissões em toneladas |
| Status | Crítico / Alerta / Moderado / Estável |

![Tela 6 - Home](./screenshots/tela6_home.png)

---

### Tela 7 — Detalhe da Área

Exibe os detalhes completos de uma área selecionada: nome, bioma, badge de status, cards com hectares desmatados e emissões de CO₂, e descrição detalhada da situação da área. Os dados são passados via argumento de navegação (`MonitoredArea`).

> **Rota:** `/area-detail`

![Tela 7 - Detalhe da Área](./screenshots/tela7_area_detalhe.png)

---

### Tela 8 — Emissões de Carbono

Lista todas as fontes de emissão de CO₂ com filtro por categoria (Todas, Uso do Solo, Agricultura, Transporte, Energia, Indústria, Resíduos). Cada card exibe nome da fonte, categoria, descrição e volume em toneladas de CO₂.

> **Rota:** `/emissions`

![Tela 8 - Emissões](./screenshots/tela8_emissoes.png)

---

### Tela 9 — Sobre o Florestae

Exibe informações sobre o projeto: logo, versão, descrição, lista de funcionalidades e os desenvolvedores responsáveis.

> **Rota:** `/about`

![Tela 9 - Sobre](./screenshots/tela9_sobre.png)


---

> 🌎 *Florestae — FIAP Global Solution 2026*
