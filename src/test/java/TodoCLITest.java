import org.junit.jupiter.api.*;
import todo.Task;
import todo.TaskManager;
import todo.TodoCLI;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TodoCLITest {
    
    private final ByteArrayOutputStream outputContent = new ByteArrayOutputStream();
    private PrintStream originalOut;
    private Object scanner;

    @BeforeEach
    void setUpStreams() {
        originalOut = System.out;
        System.setOut(new PrintStream(outputContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }
    
    @Test
    void testAddTask() {

        // Simula a entrada do usuário para adicionar uma tarefa
        String simulatedInput = String.join(System.lineSeparator(),
                "Tarefa Teste", // Nome
                "Descrição da tarefa", // Descrição
                LocalDate.now().toString(), // Data de término
                LocalTime.now().plusHours(1).toString(), // Hora de término
                "3", // Prioridade
                "Trabalho", // Categoria
                "TODO", // Status
                "s" // Ativar alarme
        );
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        TaskManager manager = new TaskManager();
        TodoCLI cli = new TodoCLI(manager, scanner, "test_tasks.json");
        
        // Executa apenas o metodo addTask diretamente
        cli.addTask();
        
        // Verifica se a tarefa foi adicionada corretamente
        Assertions.assertEquals(1, manager.getTasks().size());
        Task t = manager.getTasks().get(0);
        Assertions.assertEquals("Tarefa Teste", t.getName());
        Assertions.assertEquals("Descrição da tarefa", t.getDescription());
        Assertions.assertEquals(3, t.getPriority());
        Assertions.assertEquals("Trabalho", t.getCategory());
        Assertions.assertEquals(Task.Status.TODO, t.getStatus());
        assertTrue(t.isAlarmeAtivo());
        
        // Verifica se a saída contém a mensagem de sucesso
        assertTrue(outputContent.toString().contains("Tarefa adicionada!"));
    }

    @Test
    void removeTaskTest() {
        // Simula a entrada do usuário para remover uma tarefa
        String simulatedInput = String.join(System.lineSeparator(),
                "Tarefa Teste" // Nome da tarefa a remover
        );
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        TaskManager manager = new TaskManager();
        Task t = new Task("Tarefa Teste", "Descrição", LocalDate.now(), LocalTime.now().plusHours(1), 3, "Trabalho", Task.Status.TODO);
        manager.addTask(t);
        TodoCLI cli = new TodoCLI(manager, scanner, "test_tasks.json");

        // Executa apenas o metodo removeTask diretamente
        cli.removeTask();

        // Verifica se a tarefa foi removida corretamente
        Assertions.assertEquals(0, manager.getTasks().size());

        // Verifica se a saída contém a mensagem de sucesso
        assertTrue(outputContent.toString().contains("Removida!"));

    }

     @Test
    void listTasksTest() {
        TaskManager manager = new TaskManager();
        Task t1 = new Task("Tarefa 1", "Descrição 1", LocalDate.now(), LocalTime.now().plusHours(1), 3, "Trabalho", Task.Status.TODO);
        Task t2 = new Task("Tarefa 2", "Descrição 2", LocalDate.now(), LocalTime.now().plusHours(2), 2, "Pessoal", Task.Status.IN_PROGRESS);
        manager.addTask(t1);
        manager.addTask(t2);
        Scanner scanner = new Scanner(System.in); // Scanner não será usado neste teste
        TodoCLI cli = new TodoCLI(manager, scanner, "test_tasks.json");

        // Executa apenas o metodo listTasks diretamente
        cli.listTasks();

        // Verifica se a saída contém as tarefas adicionadas
        String output = outputContent.toString();
        assertTrue(output.contains("Tarefa 1"));
        assertTrue(output.contains("Tarefa 2"));

    }

    @Test
    void listByCategoryTest() {
        // Simula a entrada do usuário para listar por categoria
        TaskManager manager = new TaskManager();
        Task test1 = new Task("Tarefa 1", "Descrição 1", LocalDate.now(), LocalTime.now().plusHours(1), 3, "Trabalho", Task.Status.TODO);
        Task test2 = new Task("Tarefa 2", "Descrição 2", LocalDate.now(), LocalTime.now().plusHours(2), 2, "Pessoal", Task.Status.IN_PROGRESS);
        manager.addTask(test1);
        manager.addTask(test2);

        // Simula a entrada do usuário para listar por categoria
        String simulatedInput = "Trabalho"; // Categoria a ser listada
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        TodoCLI cli = new TodoCLI(manager, scanner, "test_tasks.json");

        // Executa apenas o metodo listByCategory diretamente
        cli.listByCategory();

        // Verifica se a saída contém apenas a tarefa da categoria "Trabalho"
        String output = outputContent.toString();

        // Deve conter a tarefa da categoria "Trabalho"
        assertTrue(output.contains("Tarefa 1"));
        // Não deve conter a tarefa da categoria "Pessoal"
        assertTrue(!output.contains("Tarefa 2"));

    }

    @Test
    void listByPriorityTest() {
        // Simula a entrada do usuário para listar por prioridade
        TaskManager manager = new TaskManager();
        Task test1 = new Task("Tarefa 1", "Descrição 1", LocalDate.now(), LocalTime.now().plusHours(1), 3, "Trabalho", Task.Status.TODO);
        Task test2 = new Task("Tarefa 2", "Descrição 2", LocalDate.now(), LocalTime.now().plusHours(2), 2, "Pessoal", Task.Status.IN_PROGRESS);
        manager.addTask(test1);
        manager.addTask(test2);

        // Simula a entrada do usuário para listar por prioridade
        String simulatedInput = "3"; // Prioridade a ser listada
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        TodoCLI cli = new TodoCLI(manager, scanner, "test_tasks.json");

        // Executa apenas o metodo listByPriority diretamente
        cli.listByPriority();

        // Verifica se a saída contém apenas a tarefa da prioridade 3
        String output = outputContent.toString();

        // Deve conter a tarefa da prioridade 3
        assertTrue(output.contains("Tarefa 1"));
        // Não deve conter a tarefa da prioridade 2
        assertTrue(!output.contains("Tarefa 2"));

    }

    @Test
    void listByStatusTest() {
        // Simula a entrada do usuário para listar por status
        TaskManager manager = new TaskManager();
        Task test1 = new Task("Tarefa 1", "Descrição 1", LocalDate.now(), LocalTime.now().plusHours(1), 3, "Trabalho", Task.Status.TODO);
        Task test2 = new Task("Tarefa 2", "Descrição 2", LocalDate.now(), LocalTime.now().plusHours(2), 2, "Pessoal", Task.Status.IN_PROGRESS);
        manager.addTask(test1);
        manager.addTask(test2);

        // Simula a entrada do usuário para listar por status
        String simulatedInput = "TODO"; // Status a ser listado
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        TodoCLI cli = new TodoCLI(manager, scanner, "test_tasks.json");

        // Executa apenas o metodo listByStatus diretamente
        cli.listByStatus();

        // Verifica se a saída contém apenas a tarefa do status "TO-DO"
        String output = outputContent.toString();

        // Deve conter a tarefa do status "TO-DO"
        assertTrue(output.contains("Tarefa 1"));
        // Não deve conter a tarefa do status "IN_PROGRESS"
        assertTrue(!output.contains("Tarefa 2"));

    }

    @Test
    void countByStatusTest() {
        // Simula a entrada do usuário para contar por status
        TaskManager manager = new TaskManager();
        Task test1 = new Task("Tarefa 1", "Descrição 1", LocalDate.now(), LocalTime.now().plusHours(1), 3, "Trabalho", Task.Status.TODO);
        Task test2 = new Task("Tarefa 2", "Descrição 2", LocalDate.now(), LocalTime.now().plusHours(2), 2, "Pessoal", Task.Status.IN_PROGRESS);
        Task test3 = new Task("Tarefa 3", "Descrição 3", LocalDate.now(), LocalTime.now().plusHours(3), 1, "Pessoal", Task.Status.TODO);
        manager.addTask(test1);
        manager.addTask(test2);
        manager.addTask(test3);

        Scanner scanner = new Scanner(System.in); // Scanner não será usado neste teste
        TodoCLI cli = new TodoCLI(manager, scanner, "test_tasks.json");
        // Executa apenas o metodo countByStatus diretamente
        cli.countByStatus();
        // Verifica se a saída contém a contagem correta
        String output = outputContent.toString();
        assertTrue(output.contains("TODO: 2"));
        assertTrue(output.contains("IN_PROGRESS: 1"));
        assertTrue(output.contains("DONE: 0"));

    }

    @Test
    void readDateTest() {
        // Simula a entrada do utilizador para ler uma data
        String simulatedInput = "2024-12-31"; // Data válida
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        TaskManager manager = new TaskManager();
        TodoCLI cli = new TodoCLI(manager, scanner, "test_tasks.json");

        // Chama o metodo readDate diretamente
        LocalDate date = cli.readDate("Data de término (AAAA-MM-DD): ");

        // Verifica se a data foi lida corretamente
        Assertions.assertEquals(LocalDate.of(2024, 12, 31), date);

    }

    @Test
    void readTimeTest() {
        // Simula a entrada do usuário para ler uma hora
        String simulatedInput = "14:30"; // Hora válida
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        TaskManager manager = new TaskManager();
        TodoCLI cli = new TodoCLI(manager, scanner, "test_tasks.json");

        // Chama o metodo readTime diretamente
        LocalTime time = cli.readTime("Horário de término (HH:mm): ");

        // Verifica se a hora foi lida corretamente
        Assertions.assertEquals(LocalTime.of(14, 30), time);

    }

    @Test
    void readIntTest() {
        // Simula a entrada do usuário para ler um inteiro
        String simulatedInput = "5"; // Inteiro válido
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        TaskManager manager = new TaskManager();
        TodoCLI cli = new TodoCLI(manager, scanner, "test_tasks.json");

        // Chama o metodo readInt diretamente
        int value = cli.readInt("Escolha uma opção: ");

        // Verifica se o inteiro foi lido corretamente
        Assertions.assertEquals(5, value);

    }

    @Test
    void updateTaskTest() {
        // Cria tarefa inicial
        TaskManager manager = new TaskManager();
        Task t = new Task("Tarefa Original", "Descrição Original", LocalDate.now(), LocalTime.now().plusHours(1), 3, "Trabalho", Task.Status.TODO);
        manager.addTask(t);

        // Simula a entrada do usuário para atualizar a tarefa
        String simulatedInput = String.join(System.lineSeparator(),
                "Tarefa Original", // Nome da tarefa a atualizar
                "Tarefa Atualizada", // Novo nome
                "Descrição Atualizada", // Nova descrição
                LocalDate.now().plusDays(1).toString(), // Nova data de término
                LocalTime.now().plusHours(2).toString(), // Nova hora de término
                "4", // Nova prioridade
                "Pessoal", // Nova categoria
                "IN_PROGRESS", // Novo status
                "n" // Desativar alarme
        );
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        TodoCLI cli = new TodoCLI(manager, scanner, "test_tasks.json");

        // Executa apenas o metodo updateTask diretamente
        cli.updateTask();

        // Verifica se os campos foram atualizados corretamente
        Task updated = manager.getTasks().get(0);
        Assertions.assertEquals("Tarefa Atualizada", updated.getName());
        Assertions.assertEquals("Descrição Atualizada", updated.getDescription());
        Assertions.assertEquals(LocalDate.now().plusDays(1), updated.getDueDate());
        Assertions.assertEquals(4, updated.getPriority());
        Assertions.assertEquals("Pessoal", updated.getCategory());
        Assertions.assertEquals(Task.Status.IN_PROGRESS, updated.getStatus());
        Assertions.assertFalse(updated.isAlarmeAtivo());

        // Verifica se a saída contém a mensagem de sucesso
        assertTrue(outputContent.toString().contains("Tarefa atualizada!"));
    }

    @Test
    void listBydueDateTest() {
        TaskManager manager = new TaskManager();
        Task teste = new Task("Tarefa 1", "Descrição 1", LocalDate.now().plusDays(1), LocalTime.now().plusHours(1), 3, "Trabalho", Task.Status.TODO);
        Task teste2 = new Task("Tarefa 2", "Descrição 2", LocalDate.now().plusDays(2), LocalTime.now().plusHours(2), 2, "Pessoal", Task.Status.IN_PROGRESS);
        manager.addTask(teste);
        manager.addTask(teste2);

        String simulatedInput = LocalDate.now().plusDays(1).toString();
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        TodoCLI cli = new TodoCLI(manager, scanner, "test_tasks.json");

        cli.listByDueDate();

        String output = outputContent.toString();
        Assertions.assertTrue(output.contains("Tarefa 1"));
        Assertions.assertFalse(output.contains("Tarefa 2"));
    }

    @Test
    void readStatusTest() {
        // Simula a entrada do usuário para ler um status
        String simulatedInput = "DONE"; // Status válido
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        TaskManager manager = new TaskManager();
        TodoCLI cli = new TodoCLI(manager, scanner, "test_tasks.json");

        // Chama o metodo readStatus diretamente
        Task.Status status = cli.readStatus();

        // Verifica se o status foi lido corretamente
        Assertions.assertEquals(Task.Status.DONE, status);

    }

}
