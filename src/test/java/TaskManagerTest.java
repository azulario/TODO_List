import org.junit.jupiter.api.Test;
import todo.Task;
import todo.TaskManager;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class TaskManagerTest {
    /**
     * Testa se o método addTask adiciona uma tarefa corretamente e se removeTask remove a tarefa pelo nome.
     * Verifica se o número de tarefas é atualizado após cada operação.
     */
    @Test
    void testAddAndRemoveTask() {
        TaskManager manager = new TaskManager();
        Task task = new Task("Teste", "Descrição", LocalDate.now(), java.time.LocalTime.of(12, 0), 3, "Categoria", Task.Status.TODO);
        manager.addTask(task);

        assertEquals(1, manager.getTasks().size());
        assertTrue(manager.removeTask("Teste"));
        assertEquals(0, manager.getTasks().size());
    }

    // TODO: Fazer teste de addTask() com prioridade máxima e mínima
    // TODO: Fazer teste de addTask() com nome duplicado - deve permitir ou não?
    // TODO: Fazer teste de removeTask() com nome inexistente - deve retornar false
    // TODO: Fazer teste de getTasks() com lista vazia
    // TODO: Fazer teste de getTasks() após múltiplas adições e remoções

}
