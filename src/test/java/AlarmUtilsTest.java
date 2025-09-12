import org.junit.jupiter.api.*;
import todo.AlarmUtils;
import todo.Task;
import todo.TaskManager;

import java.io.*;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class AlarmUtilsTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testChecarAlarmes_AlarmeAtivoDentroDeUmaHora() {
        TaskManager manager = new TaskManager();
        Task task = new Task("Tarefa teste", "Descricao", LocalDate.now(), LocalTime.now().plusMinutes(30), 3, "Trabalho", Task.Status.TODO);

        task.setAlarmeAtivo(true);
        manager.addTask(task);

        AlarmUtils.checarAlarmes(manager);

        String output = outContent.toString().trim();
        assertTrue(output.contains("ALERTA: Tarefa 'Tarefa teste' termina em"));
    }

    @Test
    public void testChecarAlarmes_AlarmeAtivoForaDeUmaHora() {
        TaskManager manager = new TaskManager();
        Task task = new Task("Tarefa teste", "Descricao", LocalDate.now(), LocalTime.now().plusHours(2), 3, "Trabalho", Task.Status.TODO);

        task.setAlarmeAtivo(true);
        manager.addTask(task);

        AlarmUtils.checarAlarmes(manager);

        String output = outContent.toString();
        assertFalse(output.contains("ALERTA"));
    }

}
