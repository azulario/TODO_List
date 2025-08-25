// Desenvolvido por Nathalia Veiga - ZG-Hero Project (K1-T3)
package todo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static TaskManager manager = new TaskManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        manager.setTasks(TaskPersistence.load());
        System.out.println("TODO List - ZG-Hero Project (K1-T3) - Desenvolvido por Nathalia Veiga");
        int op;
        do {
            showMenu();
            AlarmUtils.checarAlarmes(manager); // Checa alarmes após mostrar o menu
            op = readInt("Escolha uma opção: ");
            switch (op) {
                case 1: addTask(); break;
                case 2: removeTask(); break;
                case 3: listTasks(); break;
                case 4: listByCategory(); break;
                case 5: listByPriority(); break;
                case 6: listByStatus(); break;
                case 7: countByStatus(); break;
                case 8: updateTask(); break;
                case 9: listByDueDate(); break;
                case 0: System.out.println("Saindo..."); break;
                default: System.out.println("Opção inválida!");
            }
            TaskPersistence.save(manager.getTasks());
        } while (op != 0);
    }

    private static void showMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Adicionar tarefa");
        System.out.println("2. Remover tarefa");
        System.out.println("3. Listar todas as tarefas");
        System.out.println("4. Listar por categoria");
        System.out.println("5. Listar por prioridade");
        System.out.println("6. Listar por status");
        System.out.println("7. Contar tarefas por status");
        System.out.println("8. Atualizar tarefa");
        System.out.println("9. Listar por data de término");
        System.out.println("0. Sair");
    }

    private static void addTask() {
        System.out.print("Nome: ");
        String name = scanner.nextLine();
        System.out.print("Descrição: ");
        String desc = scanner.nextLine();
        LocalDate date = readDate("Data de término (AAAA-MM-DD): ");
        LocalTime time = readTime("Horário de término (HH:mm): ");
        int priority = readInt("Prioridade (1-5): ");
        System.out.print("Categoria: ");
        String category = scanner.nextLine();
        Task.Status status = readStatus();
        Task t = new Task(name, desc, date, time, priority, category, status);
        System.out.print("Ativar alarme para esta tarefa? (s/n): ");
        String alarme = scanner.nextLine().trim().toLowerCase();
        t.setAlarmeAtivo(alarme.equals("s") || alarme.equals("sim"));
        manager.addTask(t);
        System.out.println("Tarefa adicionada!");
    }

    private static void removeTask() {
        System.out.print("Nome da tarefa a remover: ");
        String name = scanner.nextLine();
        if (manager.removeTask(name)) System.out.println("Removida!");
        else System.out.println("Tarefa não encontrada.");
    }

    private static void listTasks() {
        List<Task> tasks = manager.getTasks();
        if (tasks.isEmpty()) System.out.println("Nenhuma tarefa cadastrada.");
        else tasks.forEach(System.out::println);
    }

    private static void listByCategory() {
        System.out.print("Categoria: ");
        String cat = scanner.nextLine();
        List<Task> tasks = manager.listByCategory(cat);
        if (tasks.isEmpty()) System.out.println("Nenhuma tarefa encontrada.");
        else tasks.forEach(System.out::println);
    }

    private static void listByPriority() {
        int p = readInt("Prioridade (1-5): ");
        List<Task> tasks = manager.listByPriority(p);
        if (tasks.isEmpty()) System.out.println("Nenhuma tarefa encontrada.");
        else tasks.forEach(System.out::println);
    }

    private static void listByStatus() {
        Task.Status s = readStatus();
        List<Task> tasks = manager.listByStatus(s);
        if (tasks.isEmpty()) System.out.println("Nenhuma tarefa encontrada.");
        else tasks.forEach(System.out::println);
    }

    private static void countByStatus() {
        for (Task.Status s : Task.Status.values()) {
            System.out.printf("%s: %d\n", s, manager.countByStatus(s));
        }
    }

    private static void updateTask() {
        System.out.print("Nome da tarefa a atualizar: ");
        String name = scanner.nextLine();
        Task t = manager.getTaskByName(name);
        if (t == null) {
            System.out.println("Tarefa não encontrada.");
            return;
        }
        System.out.print("Novo nome (ou Enter p/ manter): ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) t.setName(newName);
        System.out.print("Nova descrição (ou Enter p/ manter): ");
        String newDesc = scanner.nextLine();
        if (!newDesc.isEmpty()) t.setDescription(newDesc);
        System.out.print("Nova data de término (AAAA-MM-DD ou Enter p/ manter): ");
        String newDate = scanner.nextLine();
        if (!newDate.isEmpty()) t.setDueDate(LocalDate.parse(newDate));
        System.out.print("Novo horário de término (HH:mm ou Enter p/ manter): ");
        String newTime = scanner.nextLine();
        if (!newTime.isEmpty()) t.setEndTime(LocalTime.parse(newTime));
        System.out.print("Nova prioridade (1-5 ou Enter p/ manter): ");
        String newPrio = scanner.nextLine();
        if (!newPrio.isEmpty()) t.setPriority(Integer.parseInt(newPrio));
        System.out.print("Nova categoria (ou Enter p/ manter): ");
        String newCat = scanner.nextLine();
        if (!newCat.isEmpty()) t.setCategory(newCat);
        System.out.print("Novo status (TODO, DOING, DONE ou Enter p/ manter): ");
        String newStatus = scanner.nextLine();
        if (!newStatus.isEmpty()) t.setStatus(Task.Status.valueOf(newStatus.toUpperCase()));
        System.out.print("Ativar alarme para esta tarefa? (s/n ou Enter p/ manter): ");
        String alarme = scanner.nextLine().trim().toLowerCase();
        if (!alarme.isEmpty()) t.setAlarmeAtivo(alarme.equals("s") || alarme.equals("sim"));
        manager.updateTask(name, t);
        System.out.println("Tarefa atualizada!");
    }

    private static void listByDueDate() {
        LocalDate date = readDate("Data de término (AAAA-MM-DD): ");
        List<Task> tasks = manager.listByDueDate(date);
        if (tasks.isEmpty()) System.out.println("Nenhuma tarefa encontrada.");
        else tasks.forEach(System.out::println);
    }

    private static int readInt(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Valor inválido!");
            }
        }
    }

    private static LocalDate readDate(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return LocalDate.parse(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Data inválida!");
            }
        }
    }

    private static Task.Status readStatus() {
        while (true) {
            System.out.print("Status (TODO, DOING, DONE): ");
            String s = scanner.nextLine().toUpperCase();
            try {
                return Task.Status.valueOf(s);
            } catch (Exception e) {
                System.out.println("Status inválido!");
            }
        }
    }

    private static LocalTime readTime(String msg) {
        while (true) {
            System.out.print(msg);
            String input = scanner.nextLine();
            try {
                return LocalTime.parse(input);
            } catch (Exception e) {
                System.out.println("Horário inválido!");
            }
        }
    }
}
