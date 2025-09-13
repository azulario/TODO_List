// Desenvolvido por Nathalia Veiga - ZG-Hero Project (K1-T3)


// Classe principal que inicializa o programa TO-DO List, responsável apenas por carregar as tarefas e delegar a execução para a ‘interface’ de linha de comando (TodoCLI).

package todo;

import java.util.Scanner;

public class Main {
    private static final String TEST_FILE = "tasks.json";
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        manager.setTasks(TaskPersistence.load(TEST_FILE));
        System.out.println("TODO List - ZG-Hero Project (K1-T3) - Desenvolvido por Nathalia Veiga");
        TodoCLI cli = new TodoCLI(manager, new Scanner(System.in), TEST_FILE);
        cli.run();
    }
}
