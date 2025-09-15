package todo;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.time.LocalTime;

/**
 * Classe responsável por salvar e carregar tarefas em um arquivo JSON simples.
 * Utiliza métodos estáticos para serializar e desserializar objetos Task,
 * armazenando cada tarefa como uma linha no arquivo 'tasks.json`.
 * A serialização e desserialização são feitas manualmente, sem uso de bibliotecas externas.
 */

public class TaskPersistence {

    public static void save(List<Task> tasks, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Task task : tasks) {
                writer.println(serialize(task));
            }
        } catch (IOException error) {
            System.out.println("Erro ao salvar tarefas: " + error.getMessage());
        }
    }

    public static List<Task> load(String fileName) {
        List<Task> tasks = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists())  return tasks;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) !=null) {
                Task task = deserialize(line);
                if (task != null) tasks.add(task);
            }
        } catch (IOException error) {
            System.out.println("Erro ao carregar tarefas: " + error.getMessage());
        }
        return tasks;
    }

    // Serialização simples em JSON (manual)
    private static String serialize(Task task) {// Serializa cada campo, convertendo null para string vazia
        return String.format(Locale.US,
            "{\"name\":\"%s\",\"description\":\"%s\",\"dueDate\":\"%s\",\"endTime\":\"%s\",\"priority\":%d,\"category\":\"%s\",\"status\":\"%s\",\"alarmeAtivo\":%b}",
            escape(task.getName()),
            escape(task.getDescription()),
            task.getDueDate() == null ? "" : task.getDueDate().toString(),
            task.getEndTime() == null ? "" : task.getEndTime().toString(),
            task.getPriority(),
            escape(task.getCategory()),
            task.getStatus() == null ? "" : task.getStatus().name(),
            task.isAlarmeAtivo()
        );
    }

/**
 * Classe responsável por persistir objetos Task num arquivo local no formato JSON (um por linha).
 * Não utiliza bibliotecas externas para serialização/desserialização: converte manualmente os campos
 * dos objetos Task para uma ‘string’ JSON e vice-versa.
 *
 * Métodos principais:
 * - save: salva uma lista de tarefas no arquivo, sobrescrevendo o conteúdo anterior.
 * - load: carrega todas as tarefas do arquivo, reconstruindo os objetos Task.
 *
 * Cada linha do arquivo representa uma tarefa serializada em JSON simples.
 * O metodo serialize converte um Task em ‘string’ JSON.
 * O metodo deserialize reconstrói um Task a partir da ‘string’ JSON.
 */

    private static Task deserialize(String json) {
        try {
            Map<String, String> map = new HashMap<>();
            json = json.trim().replaceAll("\"", "");
            String[] parts = json.split(",");
            for (String part : parts) {
                String[] keyValue = part.split(":", 2);
                if (keyValue.length == 2) map.put(keyValue[0], keyValue[1]);
            }

            String name = map.get("name");
            if (name != null && name.isEmpty()) name = null;

            String category = map.get("category");
            if (category != null && category.isEmpty()) category = null;

            String endTimeStr = map.get("endTime");
            LocalTime endTime = (endTimeStr == null || endTimeStr.isEmpty()) ? null : LocalTime.parse(endTimeStr);

            String priorityStr = map.get("priority");
            int priority = (priorityStr == null || priorityStr.isEmpty()) ? 0 : Integer.parseInt(priorityStr);

            String dueDateStr = map.get("dueDate");
            LocalDate dueDate = (dueDateStr == null || dueDateStr.isEmpty()) ? null : LocalDate.parse(dueDateStr);

            String description = map.get("description");
            if (description != null && description.isEmpty()) description = null;


            Task.Status status = (map.get("status") == null || map.get("status").isEmpty()) ? null : Task.Status.valueOf(map.get("status"));

            Task task = new Task(
                    name,
                    description,
                    dueDate,
                    endTime,
                    priority,
                    category,
                    status
            );

            if (map.containsKey("alarmeAtivo")) {
                String val = map.get("alarmeAtivo").trim().toLowerCase();
                task.setAlarmeAtivo(val.equals("true"));
            }
            return task;

        } catch (Exception e) {
           return null;
        }
    }

    private static String escape(String string) {
        if (string == null) return "";
        return string.replace("\"", "\\\"");
    }
}
