package todo;

import java.io.*;
import java.util.*;
import java.time.LocalTime;

/**
 * Classe responsável por salvar e carregar tarefas em um arquivo JSON simples.
 * Utiliza métodos estáticos para serializar e desserializar objetos Task,
 * armazenando cada tarefa como uma linha no arquivo `tasks.json`.
 * A serialização e desserialização são feitas manualmente, sem uso de bibliotecas externas.
 */

public class TaskPersistence {
    private static final String FILE_NAME = "tasks.json";

    public static void save(List<Task> tasks, String testFile) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Task t : tasks) {
                writer.println(serialize(t));
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar tarefas: " + e.getMessage());
        }
    }

    public static List<Task> load(String testFile) {
        List<Task> tasks = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return tasks;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task t = deserialize(line);
                if (t != null) tasks.add(t);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar tarefas: " + e.getMessage());
        }
        return tasks;
    }

    // Serialização simples em JSON (manual)
    private static String serialize(Task t) {
        return String.format(Locale.US,
            "{\"name\":\"%s\",\"description\":\"%s\",\"dueDate\":\"%s\",\"endTime\":\"%s\",\"priority\":%d,\"category\":\"%s\",\"status\":\"%s\",\"alarmeAtivo\":%b}",
            escape(t.getName()), escape(t.getDescription()), t.getDueDate(), t.getEndTime(), t.getPriority(), escape(t.getCategory()), t.getStatus(), t.isAlarmeAtivo());
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
            json = json.trim().replaceAll("[{}\"]", "");
            String[] parts = json.split(",");
            for (String part : parts) {
                String[] kv = part.split(":", 2);
                if (kv.length == 2) map.put(kv[0].trim(), kv[1].trim());
            }
            LocalTime endTime = LocalTime.of(13, 0); // valor padrão
            if (map.containsKey("endTime")) {
                try { endTime = LocalTime.parse(map.get("endTime")); } catch(Exception ignored) {}
            }
            Task task = new Task(
                map.get("name"),
                map.get("description"),
                java.time.LocalDate.parse(map.get("dueDate")),
                endTime,
                Integer.parseInt(map.get("priority")),
                map.get("category"),
                Task.Status.valueOf(map.get("status"))
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

    private static String escape(String s) {
        return s.replace("\"", "\\\"");
    }
}
