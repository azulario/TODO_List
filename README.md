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
- Cadastro e edição de tarefas com data **e horário** de término
- Ativação de alarme por tarefa
- Alerta automático no terminal quando faltar até 1 hora para o término de uma tarefa com alarme ativo
- Filtrar por categoria, prioridade, status e data de término
- Contar tarefas por status
- Persistência automática em arquivo `tasks.json`

## Exemplo de tarefa no arquivo `tasks.json`
```json
{"name":"Comprar leite","description":"Ir ao mercado","dueDate":"2025-08-25","endTime":"15:00","priority":2,"category":"Compras","status":"TODO","alarmeAtivo":true}
```
Cada linha representa uma tarefa.

## Observações
- O frontend será implementado futuramente.
- O rebalanceamento das tarefas é feito automaticamente pela prioridade (maior prioridade primeiro).
- O nome do desenvolvedor está no início do arquivo `Main.java`.
- Mensagens de debug foram removidas: apenas alertas reais de alarme aparecem no terminal.

---

> Projeto para fins educacionais - ZG Acelera
