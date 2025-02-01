import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ToDoList {

    static String filePath = "tasks.txt";
    static List<Task> taskList = loadTasksFromFile(filePath);

    public static void saveTasksInFile(List<Task> taskList, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (Task task : taskList) {
                writer.write(taskToLine(task));
                writer.newLine();
            }
        } catch (IOException ex) {
            System.err.println("Erro ao salvar as tarefas no arquivo: " + ex.getMessage());
            throw ex;
        }
        System.out.println("Tarefas salvas com sucesso no arquivo!");
    }

    private static String taskToLine(Task task) {
        return String.join(";",
                String.valueOf(task.getID()),
                task.getName(),
                task.getDescription(),
                task.getCategory(),
                task.getStatus(),
                task.getFinnishDate().toString(),
                String.valueOf(task.getPriorityLevel())
        );
    }

    public static List<Task> loadTasksFromFile(String filePath) {
        List<Task> taskList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = lineToTask(line);
                taskList.add(task);
            }
            System.out.println("Tarefas carregadas com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao carregar as tarefas: " + e.getMessage());
        }
        return taskList;
    }

    private static Task lineToTask(String line) {
        String[] fields = line.split(";");
        return new Task(
                fields[1], // Name
                fields[2], // Description
                fields[3], // Category
                fields[4], // Status
                LocalDate.parse(fields[5]), // Finish date
                Integer.parseInt(fields[6]), // Priority
                Integer.parseInt(fields[0]) // ID
        );
    }

    public static void addTask(Scanner scanner) {
        System.out.print("Digite o nome da tarefa: ");
        String name = scanner.nextLine();

        System.out.print("Digite a descrição da tarefa: ");
        String description = scanner.nextLine();

        System.out.print("Digite a categoria da tarefa: ");
        String category = scanner.nextLine();

        System.out.print("Digite o status da tarefa (To Do, Doing, Done): ");
        String status = scanner.nextLine();

        System.out.print("Digite a data de término (formato: AAAA-MM-DD): ");
        LocalDate finnishDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Digite o nível de prioridade (1 a 5): ");
        int priorityLevel = scanner.nextInt();

        System.out.print("Digite o ID da tarefa: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Task newTask = new Task(name, description, category, status, finnishDate, priorityLevel, id);
        taskList.add(newTask);
        System.out.println("Tarefa cadastrada com sucesso!");
    }

    public static void deleteLineById(String idToRemove) {
        List<String> linesToKeep = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;

            // Read and filter the lines
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith(idToRemove + ";")) {
                    found = true; // Line found, does not add to list
                    continue;
                }
                linesToKeep.add(currentLine); // Add line that will be kept
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            return;
        }
        if (!found) {
            System.out.println("ID " + idToRemove + " não encontrado.");
            return;
        }

        // Overwrite the file with the filtered lines
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : linesToKeep) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao sobrescrever o arquivo: " + e.getMessage());
        }
        taskList = loadTasksFromFile(filePath);
        System.out.println("Tarefa com ID " + idToRemove + " removida com sucesso!");
    }

    public static void updateTask(Scanner scanner) {
        System.out.print("Digite o ID da tarefa que deseja atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Task taskToUpdate = null;
        for (Task task : taskList) {
            if (task.getID() == id) {
                taskToUpdate = task;
                break;
            }
        }

        if (taskToUpdate == null) {
            System.out.println("Tarefa com ID " + id + " não encontrada.");
            return;
        }

        boolean keepUpdating = true;
        do {
            System.out.println("\nSelecione o campo para atualizar:");
            System.out.println("1. Nome");
            System.out.println("2. Descrição");
            System.out.println("3. Categoria");
            System.out.println("4. Status");
            System.out.println("5. Data de término");
            System.out.println("6. Nível de prioridade");
            System.out.print("Opção: ");

            int field;
            try {
                field = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite um número entre 1-6.");
                continue;
            }

            switch (field) {
                case 1:
                    System.out.print("Novo nome: ");
                    taskToUpdate.setName(scanner.nextLine());
                    break;
                case 2:
                    System.out.print("Nova descrição: ");
                    taskToUpdate.setDescription(scanner.nextLine());
                    break;
                case 3:
                    System.out.print("Nova categoria: ");
                    taskToUpdate.setCategory(scanner.nextLine());
                    break;
                case 4:
                    System.out.print("Novo status (To Do, Doing, Done): ");
                    taskToUpdate.setStatus(scanner.nextLine());
                    break;
                case 5:
                    System.out.print("Nova data (AAAA-MM-DD): ");
                    try {
                        taskToUpdate.setFinnishDate(LocalDate.parse(scanner.nextLine()));
                    } catch (Exception e) {
                        System.out.println("Formato de data inválido!");
                    }
                    break;
                case 6:
                    System.out.print("Nova prioridade (1-5): ");
                    try {
                        taskToUpdate.setPriorityLevel(Integer.parseInt(scanner.nextLine()));
                    } catch (Exception e) {
                        System.out.println("Prioridade deve ser um número!");
                    }
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

            System.out.print("Deseja alterar outro campo? (S/N): ");
            String answer = scanner.nextLine().trim().toUpperCase();
            keepUpdating = answer.equals("S");

        } while (keepUpdating);

        try {
            saveTasksInFile(taskList, filePath);
            System.out.println("Tarefa atualizada com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar alterações: " + e.getMessage());
        }
    }

    public static void listByCategory(Scanner scanner) {
        System.out.print("Digite a categoria desejada: ");
        String category = scanner.nextLine();
        System.out.println("Tarefas na categoria '" + category + "':");
        taskList.stream()
                .filter(task -> task.getCategory().equalsIgnoreCase(category))
                .forEach(System.out::println);
    }

    public static void listByStatus(String status) {
        System.out.println("Tarefas com status '" + status + "':");
        taskList.stream()
                .filter(task -> task.getStatus().equalsIgnoreCase(status))
                .forEach(System.out::println);
    }

    // Lists all tasks that have the priority passed in the parameter
    // Priority 1 - 5 (1 == low priority. 5 == high priority)
    public static void listByPriority(int priority) {
        System.out.println("Tarefas com prioridade " + priority + ":");
        taskList.stream()
                .filter(task -> task.getPriorityLevel() == priority)
                .forEach(System.out::println);
    }

    public static void listAllTasks() {
        System.out.println("Tarefas na lista:");
        taskList.forEach(System.out::println);
    }

    public static List<Task> listByTotalPriority(List<Task> taskList) {
        taskList.sort(Comparator.comparingInt(Task::getPriorityLevel).reversed());
        return taskList;
    }

    public static void listByDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Tarefas com data de término " + date.format(formatter) + ":");
        taskList.stream()
                .filter(task -> task.getFinnishDate().isEqual(date))
                .forEach(System.out::println);
    }

    public static List<Task> listByName(List<Task> taskList, boolean ascending) {
        taskList.sort(Comparator.comparing(Task::getName));
        if (!ascending) {
            taskList.sort(Comparator.comparing(Task::getName).reversed());
        }
        return taskList;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean continue_ = true;

        while (continue_) {
            System.out.println("\n--- Menu ToDoList ---\n");
            System.out.println("1. Cadastrar tarefa");
            System.out.println("2. Listar todas as tarefas");
            System.out.println("3. Listar tarefas por nome (crescente)");
            System.out.println("4. Listar tarefas por nome (decrescente)");
            System.out.println("5. Listar tarefas por status");
            System.out.println("6. Listar tarefas por categoria");
            System.out.println("7. Listar tarefas por data de conclusão");
            System.out.println("8. Listar tarefas por prioridade (Todas as tarefas com a prioridade indicada)");
            System.out.println("9. Listar por prioridade (Ordem de prioridade)");
            System.out.println("10. Remover tarefa por ID");
            System.out.println("11. Atualizar tarefa por ID");
            System.out.println("12. Sair");
            System.out.print("\nEscolha uma opção: ");
            int option = scanner.nextInt();
            System.out.println();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    addTask(scanner);
                    saveTasksInFile(taskList, filePath);
                }
                case 2 -> listAllTasks();
                case 3 -> {
                    listByName(taskList, true);
                    for (Task task : taskList) {
                        System.out.println(task);
                    }
                }
                case 4 -> {
                    listByName(taskList, false);
                    for (Task task : taskList) {
                        System.out.println(task);
                    }
                }
                case 5 -> {
                    System.out.print("Digite o status que deseja buscar (To Do, Doing, Done): ");
                    listByStatus(scanner.nextLine());
                }
                case 6 -> listByCategory(scanner);
                case 7 -> {
                    System.out.println("Digite a data na qual deseja buscar por tarefas (DD/MM/AAAA) ");
                    System.out.print("Dia: ");
                    int date = scanner.nextInt();
                    System.out.print("Mês: ");
                    int month = scanner.nextInt();
                    System.out.print("Ano: ");
                    int year = scanner.nextInt();
                    listByDate(LocalDate.of(year, month, date));
                }
                case 8 -> {
                    System.out.print("Digite o nível de prioridade (1 a 5): ");
                    listByPriority(scanner.nextInt());
                }
                case 9 -> {
                    System.out.println("Tarefas em ordem de prioridade: ");
                    listByTotalPriority(taskList);
                    for (Task task : taskList) {
                        System.out.println(task);
                    }
                }
                case 10 -> {
                    System.out.print("Digite o ID da tarefa que deseja remover: ");
                    deleteLineById(String.valueOf(scanner.nextInt()));
                    saveTasksInFile(taskList, filePath);
                }
                case 11 -> updateTask(scanner);
                case 12 -> {
                    continue_ = false;
                    System.out.println("Saindo do programa...");
                    saveTasksInFile(taskList, filePath);
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
        scanner.close();
    }
}