# App Receitas

Aplicativo Android que permite que cada usuário cadastre, visualize e gerencie suas próprias receitas de forma prática, com imagens, seções organizadas e busca por título. Utiliza armazenamento local com SQLite e arquivos JSON.

##  Funcionalidades

- Cadastro e login de usuários com validações
- Galeria de receitas personalizada por usuário
- Cadastro de novas receitas com:
  - Imagem da receita
  - Título
  - Ingredientes
  - Recheio
  - Modo de Preparo
- Busca de receitas por título
- Salvamento persistente das receitas com JSON local
- Interface clean e acessível

## Identidade Visual

- Ícone com tema culinário (colher), nas cores branco e dourado
- Estilização com `MaterialButton`
- Layout responsivo, com cores suaves e contraste para acessibilidade
- Fontes legíveis e botões bem distribuídos

##  Tecnologias Utilizadas

- Java (lógica de programação)
- SQLite (autenticação e controle de usuários)
- JSON interno (armazenamento de receitas)
- XML (layouts)
- Glide (carregamento de imagens por URI)
- Android Studio (IDE)

## Tela Inicial

A tela inicial exibe as receitas do usuário logado, com barra de pesquisa no topo. Ao clicar em uma receita, os detalhes são exibidos em formato expansível.
