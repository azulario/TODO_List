package todo;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class AlarmUtils {
    public static void checarAlarmes(TaskManager manager) {
        LocalDateTime agora = LocalDateTime.now();
        for (Task t : manager.getTasks()) {
            if (t.isAlarmeAtivo()) {
                LocalDateTime termino = t.getDueDate().atTime(t.getEndTime());
                long diff = Duration.between(agora, termino).toMinutes();
                if (diff >= 0 && diff <= 60) {
                    System.out.println("ALERTA: Tarefa '" + t.getName() + "' termina em " + diff + " minutos!");
                }
            }
        }
    }
}
