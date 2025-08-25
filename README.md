# TODO List - ZG-Hero Project (K1-T3)

Desenvolvido por Nathalia Veiga

## Descrição
Aplicação backend em Java para gerenciamento de tarefas (TODO List), parte do desafio ZG-Hero (K1-T3). Permite criar, remover, listar, filtrar, atualizar e persistir tarefas em arquivo JSON, com menu interativo no terminal.

## Tecnologias Utilizadas
- Java (sem frameworks)
- Persistência em arquivo JSON
- Execução via terminal

## Como Executar
1. Compile o projeto:
   ```bash
   ./gradlew build
   ```
2. Execute o programa:
   ```bash
   java -cp build/classes/java/main todo.Main
   ```

## Funcionalidades
- Adicionar, remover, listar e atualizar tarefas
- Filtrar por categoria, prioridade, status e data de término
- Contar tarefas por status
- Persistência automática em arquivo `tasks.json`

## Observações
- O frontend será implementado futuramente.
- O rebalanceamento das tarefas é feito automaticamente pela prioridade (maior prioridade primeiro).
- O nome do desenvolvedor está no início do arquivo `Main.java`.

---

> Projeto para fins educacionais - ZG Acelera

