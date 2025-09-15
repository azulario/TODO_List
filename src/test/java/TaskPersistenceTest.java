import org.junit.jupiter.api.*;
import todo.Task;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public class TaskPersistenceTest {
    private static final String TEST_FILE = "test_tasks.json";

    @BeforeEach
    public void setup() {
        // Remove o arquivo de teste antes de cada teste
        new File(TEST_FILE).delete();
    }

    @AfterEach
    public void cleanup() {
        // Remove o arquivo de teste após cada teste
        new File(TEST_FILE).delete();
    }

    // Testa salvar e carregar tarefas
    @Test
    void testSaveAndLoadTasks() {
        // Cria tarefa de teste
        Task task = new Task("Task Teste", "Deescrição", LocalDate.now(), LocalTime.now().plusHours(1), 3, "Trabalho", Task.Status.TODO);
        List<Task> tasks = List.of(task);

        // Salva Usando o TaskPersistence
        todo.TaskPersistence.save(tasks,   TEST_FILE);

        // Carrega as tarefas
        List<Task> loadedTasks = todo.TaskPersistence.load(TEST_FILE);

        // Verifica se a tarefa carregada é igual à original
        Assertions.assertEquals(1, loadedTasks.size());
        Task loadedTask = loadedTasks.get(0);
        Assertions.assertEquals(task.getName(), loadedTask.getName());
        Assertions.assertEquals(task.getDescription(), loadedTask.getDescription());
        Assertions.assertEquals(task.getDueDate(), loadedTask.getDueDate());
        Assertions.assertEquals(task.getEndTime(), loadedTask.getEndTime());
        Assertions.assertEquals(task.getPriority(), loadedTask.getPriority());
        Assertions.assertEquals(task.getCategory(), loadedTask.getCategory());
        Assertions.assertEquals(task.getStatus(), loadedTask.getStatus());
        Assertions.assertEquals(task.isAlarmeAtivo(), loadedTask.isAlarmeAtivo());
    }

    @Test
    void testLoadFromNonExistentFile() {
        // Tenta carregar de um arquivo que não existe
        List<Task> loadedTasks = todo.TaskPersistence.load("non_existent_file.json");
        // Deve retornar lista vazia
        Assertions.assertTrue(loadedTasks.isEmpty());
    }

    @Test
    void testSaveEmptyTaskList() {
        // Salva uma lista vazia
        todo.TaskPersistence.save(List.of(), TEST_FILE);
        // Carrega as tarefas
        List<Task> loadedTasks = todo.TaskPersistence.load(TEST_FILE);
        // Deve retornar lista vazia
        Assertions.assertTrue(loadedTasks.isEmpty());
    }

    @Test
    void testLoadFromCorruptedFile() throws Exception {
        // Cria um arquivo corrompido
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(TEST_FILE))) {
            writer.println("This is not a valid JSON line");
        }
        // Tenta carregar as tarefas
        List<Task> loadedTasks = todo.TaskPersistence.load(TEST_FILE);
        // Deve retornar lista vazia
        Assertions.assertTrue(loadedTasks.isEmpty());
    }

    @Test
    void testSaveTaskWithNullFields() {
        // Cria tarefa com campos nulos
        Task task = new Task(null, null, null, null, 0, null, null);
        List<Task> tasks = List.of(task);

        // Salva Usando o TaskPersistence
        todo.TaskPersistence.save(tasks, TEST_FILE);

        // Carrega as tarefas
        List<Task> loadedTasks = todo.TaskPersistence.load(TEST_FILE);

        // Verifica se a tarefa carregada é igual à original
        Assertions.assertEquals(1, loadedTasks.size());
        Task loadedTask = loadedTasks.get(0);
        Assertions.assertNull(loadedTask.getName());
        Assertions.assertNull(loadedTask.getDescription());
        Assertions.assertNull(loadedTask.getDueDate());
        Assertions.assertNull(loadedTask.getEndTime());
        Assertions.assertEquals(0, loadedTask.getPriority());
        Assertions.assertNull(loadedTask.getCategory());
        Assertions.assertNull(loadedTask.getStatus());
        Assertions.assertFalse(loadedTask.isAlarmeAtivo());
    }

    // (Teste de save() - salvar lista de tarefas e verificar se o arquivo foi criado corretamente já está coberto)
    // (Teste de load() - carregar lista de tarefas de um arquivo existente e verificar se os dados batem já está coberto)
    // Coberto: Fazer teste de load() com arquivo inexistente - deve retornar lista vazia
    // Coberto: Fazer teste de save() com lista vazia - deve criar arquivo vazio ou com estrutura correta
    // COberto: Fazer teste de load() com arquivo corrompido ou inválido - deve tratar exceção e retornar lista vazia
    // Coberto: Fazer teste de save() com tarefas contendo campos nulos ou inválidos - deve tratar corretamente
    // TODO: Fazer teste de load() com múltiplas tarefas - verificar se todas são carregadas corretamente
}
