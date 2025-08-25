package todo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Task implements Serializable {
    private String name;
    private String description;
    private LocalDate dueDate;
    private LocalTime endTime;
    private int priority; // 1-5
    private String category;
    private Status status;
    private boolean alarmeAtivo;

    public enum Status {
        TODO, DOING, DONE
    }

    public Task(String name, String description, LocalDate dueDate, LocalTime endTime, int priority, String category, Status status) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.endTime = endTime;
        this.priority = priority;
        this.category = category;
        this.status = status;
        // alarmeAtivo deve ser setado via setter após a criação, ou adicione um parâmetro se necessário
    }

    // Getters e setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public boolean isAlarmeAtivo() { return alarmeAtivo; }
    public void setAlarmeAtivo(boolean alarmeAtivo) { this.alarmeAtivo = alarmeAtivo; }

    @Override
    public String toString() {
        return String.format("[Nome: %s | Desc: %s | Data: %s %s | Prioridade: %d | Categoria: %s | Status: %s]",
                name, description, dueDate, endTime, priority, category, status);
    }
}
