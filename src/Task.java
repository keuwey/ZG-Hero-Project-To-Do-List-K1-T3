import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private String Name, Description, Category, Status;
    private LocalDate FinnishDate;
    private int PriorityLevel;
    private final int ID;


    public Task(String name,
                String description,
                String category,
                String status,
                LocalDate finnishDate,
                int priorityLevel,
                int id) {

        Name = name;
        Description = description;
        Category = category;
        Status = status;
        FinnishDate = finnishDate;
        PriorityLevel = priorityLevel;
        ID = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            Name = name;
        }
        else {
            System.out.println("O nome não pode ser vazio");
        }
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        if (description != null && !description.isEmpty()) {
            Description = description;
        }
        else {
            System.out.println("A descrição não pode ser vazia");
        }
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        if (category != null && !category.isEmpty()) {
            Category = category;
        }
        else {
            System.out.println("A categoria não pode ser vazia");
        }

    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        if (status != null && !status.isEmpty()) {
            Status = status;
        }
        else {
            System.out.println("O status não pode ficar vazio");
        }
    }

    public LocalDate getFinnishDate() {
        return FinnishDate;
    }

    public void setFinnishDate(LocalDate finnishDate) {
        if (finnishDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data de conclusão não pode estar no passado!");
        }
        FinnishDate = finnishDate;
        System.out.println("Data de término atualizada para: " + finnishDate);
    }

    public int getPriorityLevel() {
        return PriorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        PriorityLevel = priorityLevel;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return
                "Nome = '" + Name + '\'' +
                        ", Descrição = '" + Description + '\'' +
                        ", Categoria = '" + Category + '\'' +
                        ", Status = '" + Status + '\'' +
                        ", Data de término = " + (FinnishDate != null ? FinnishDate.format(formatter) : "Data não definida")
                        + ", Nível de prioridade = " + PriorityLevel;
    }
}
