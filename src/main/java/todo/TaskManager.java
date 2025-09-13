package todo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe TaskManager gerencia uma lista de tarefas (Task).
 * Permite adicionar, remover, atualizar e listar tarefas por diferentes critérios,
 * como categoria, prioridade, status e data de vencimento.
 * As tarefas são automaticamente reordenadas por prioridade (da maior para a menor).
 */

// Classe responsável por gerenciar a lista de tarefas, incluindo operações de CRUD, filtragem, ordenação e manipulação das tarefas em memória.
public class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        rebalance();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
        rebalance();
    }

    public boolean removeTask(String name) {
        return tasks.removeIf(t -> t.getName().equalsIgnoreCase(name));
    }

    public List<Task> listByCategory(String category) {
        return tasks.stream().filter(t -> t.getCategory().equalsIgnoreCase(category)).collect(Collectors.toList());
    }

    public List<Task> listByPriority(int priority) {
        return tasks.stream().filter(t -> t.getPriority() == priority).collect(Collectors.toList());
    }

    public List<Task> listByStatus(Task.Status status) {
        return tasks.stream().filter(t -> t.getStatus() == status).collect(Collectors.toList());
    }

    public List<Task> listByDueDate(LocalDate date) {
        return tasks.stream().filter(t -> t.getDueDate().equals(date)).collect(Collectors.toList());
    }

    public int countByStatus(Task.Status status) {
        return (int) tasks.stream().filter(t -> t.getStatus() == status).count();
    }

    public Task getTaskByName(String name) {
        return tasks.stream().filter(t -> t.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void updateTask(String name, Task updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getName().equalsIgnoreCase(name)) {
                tasks.set(i, updatedTask);
                break;
            }
        }
        rebalance();
    }

    private void rebalance() {
        tasks.sort(Comparator.comparingInt(Task::getPriority).reversed());
    }
}
