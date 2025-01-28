import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ToDoList {

    static String caminhoArquivo = "tarefas.txt";
    static List<Tarefa> listaDeTarefas = carregarTarefasDeArquivo(caminhoArquivo);

    public static void salvarTarefasEmArquivo(List<Tarefa> listaDeTarefas, String caminhoArquivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo, false))) {
            for (Tarefa tarefa : listaDeTarefas) {
                writer.write(tarefaParaLinha(tarefa));
                writer.newLine();
            }
        } catch (IOException ex) {
            System.err.println("Erro ao salvar as tarefas no arquivo: " + ex.getMessage());
            throw ex;
        }
        System.out.println("Tarefas salvas com sucesso no arquivo!");
    }

    private static String tarefaParaLinha(Tarefa tarefa) {
        return String.join(";",
                String.valueOf(tarefa.getID()),
                tarefa.getNome(),
                tarefa.getDescricao(),
                tarefa.getCategoria(),
                tarefa.getStatus(),
                tarefa.getDataDeTermino().toString(),
                String.valueOf(tarefa.getNivelDePrioridade())
        );
    }

    public static List<Tarefa> carregarTarefasDeArquivo(String caminhoArquivo) {
        List<Tarefa> listaDeTarefas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Tarefa tarefa = linhaParaTarefa(linha);
                listaDeTarefas.add(tarefa);
            }
            System.out.println("Tarefas carregadas com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao carregar as tarefas: " + e.getMessage());
        }
        return listaDeTarefas;
    }

    private static Tarefa linhaParaTarefa(String linha) {
        String[] campos = linha.split(";");
        return new Tarefa(
                campos[1], // Nome
                campos[2], // Descrição
                campos[3], // Categoria
                campos[4], // Status
                LocalDate.parse(campos[5]), // Data de término
                Integer.parseInt(campos[6]), // Prioridade
                Integer.parseInt(campos[0]) // ID
        );
    }

    public static void adicionarTarefa(Scanner scanner) {
        System.out.print("Digite o nome da tarefa: ");
        String nome = scanner.nextLine();

        System.out.print("Digite a descrição da tarefa: ");
        String descricao = scanner.nextLine();

        System.out.print("Digite a categoria da tarefa: ");
        String categoria = scanner.nextLine();

        System.out.print("Digite o status da tarefa (To Do, Doing, Done): ");
        String status = scanner.nextLine();

        System.out.print("Digite a data de término (formato: AAAA-MM-DD): ");
        LocalDate dataDeTermino = LocalDate.parse(scanner.nextLine());

        System.out.print("Digite o nível de prioridade (1 a 5): ");
        int nivelDePrioridade = scanner.nextInt();

        System.out.print("Digite o ID da tarefa: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Tarefa novaTarefa = new Tarefa(nome, descricao, categoria, status, dataDeTermino, nivelDePrioridade, id);
        listaDeTarefas.add(novaTarefa);
        System.out.println("Tarefa cadastrada com sucesso!");
    }

    public static void deleteLineById(String idToRemove) {
        List<String> linesToKeep = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String currentLine;

            // Ler e filtrar as linhas
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith(idToRemove + ";")) {
                    found = true; // Linha encontrada, não adiciona à lista
                    continue;
                }
                linesToKeep.add(currentLine); // Adiciona linha que será mantida
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            return;
        }
        if (!found) {
            System.out.println("ID " + idToRemove + " não encontrado.");
            return;
        }

        // Sobrescrever o arquivo com as linhas filtradas
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            for (String line : linesToKeep) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao sobrescrever o arquivo: " + e.getMessage());
        }
        listaDeTarefas = carregarTarefasDeArquivo(caminhoArquivo);
        System.out.println("Tarefa com ID " + idToRemove + "removida com sucesso!");
    }

    public static void listarPorCategoria(Scanner scanner) {
        System.out.print("Digite a categoria desejada: ");
        String categoria = scanner.nextLine();
        System.out.println("Tarefas na categoria '" + categoria + "':");
        listaDeTarefas.stream()
                .filter(tarefa -> tarefa.getCategoria().equalsIgnoreCase(categoria))
                .forEach(System.out::println);
    }

    public static void listarPorStatus(String status) {
        System.out.println("Tarefas com status '" + status + "':");
        listaDeTarefas.stream()
                .filter(tarefa -> tarefa.getStatus().equalsIgnoreCase(status))
                .forEach(System.out::println);
    }

    // Lista todas as tarefas com que tenha a prioridade passada no parâmetro
    // Prioridade 1 - 5 (1 == baixa prioridade. 5 == alta prioridade)
    public static void listarPorPrioridade(int prioridade) {
        System.out.println("Tarefas com prioridade " + prioridade + ":");
        listaDeTarefas.stream()
                .filter(tarefa -> tarefa.getNivelDePrioridade() == prioridade)
                .forEach(System.out::println);
    }

    public static void listarTodasTarefas() {
        System.out.println("Tarefas na lista:");
        listaDeTarefas.forEach(System.out::println);
    }

    public static void listarPorPrioridadeDecrescente() {
        Collections.sort(listaDeTarefas, Comparator.comparingInt(Tarefa::getNivelDePrioridade).reversed());
        System.out.println("Tarefas por prioridade (decrescente):");
        listarTodasTarefas();
    }

    public static List<Tarefa> listarPorPrioridadeTotal(List<Tarefa> listaDeTarefas) {
        listaDeTarefas.sort(Comparator.comparingInt(Tarefa::getNivelDePrioridade).reversed());
        return listaDeTarefas;
    }

    public static void listarPorData(LocalDate data) {
        System.out.println("Tarefas com data de término " + data + ":");
        listaDeTarefas.stream()
                .filter(tarefa -> tarefa.getDataDeTermino().isEqual(data))
                .forEach(System.out::println);
    }

    public static List<Tarefa> listarPorNome(List<Tarefa> listaDeTarefas, boolean crescente) {
        listaDeTarefas.sort(Comparator.comparing(Tarefa::getNome));
        if (!crescente) {
            listaDeTarefas.sort(Comparator.comparing(Tarefa::getNome).reversed());
        }
        return listaDeTarefas;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n--- Menu ToDoList ---\n");
            System.out.println("1. Cadastrar tarefa");
            System.out.println("2. Listar todas as tarefas");
            System.out.println("3. Listar tarefas por nome (crescente)");
            System.out.println("4. Listar tarefas por nome (decrescente)");
            System.out.println("5. Listar tarefas por status");
            System.out.println("6. Listar tarefas por categoria");
            System.out.println("7. Listar tarefas por prioridade (Todas as tarefas com a prioridade indicada)");
            System.out.println("8. Listar por prioridade (Ordem de prioridade)");
            System.out.println("9. Remover tarefa por ID");
            System.out.println("10. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> {
                    adicionarTarefa(scanner);
                    salvarTarefasEmArquivo(listaDeTarefas, caminhoArquivo);
                }
                case 2 -> listarTodasTarefas();
                case 3 -> {
                    listarPorNome(listaDeTarefas, true);
                    for (Tarefa tarefa : listaDeTarefas) {
                        System.out.println(tarefa);
                    }
                }
                case 4 -> {
                    listarPorNome(listaDeTarefas, false);
                    for (Tarefa tarefa : listaDeTarefas) {
                        System.out.println(tarefa);
                    }
                }
                case 5 -> {
                    System.out.print("Digite o status que deseja buscar (To Do, Doing, Done): ");
                    listarPorStatus(scanner.nextLine());
                }
                case 6 -> listarPorCategoria(scanner);
                case 7 -> {
                    System.out.print("Digite o nível de prioridade (1 a 5): ");
                    listarPorPrioridade(scanner.nextInt());
                }
                case 8 -> {
                    System.out.println("Tarefas em ordem de prioridade: ");
                    listarPorPrioridadeTotal(listaDeTarefas);
                    for (Tarefa tarefa : listaDeTarefas) {
                        System.out.println(tarefa);
                    }
                }
                case 9 -> {
                    System.out.print("Digite o ID da tarefa que deseja remover: ");
                    deleteLineById(String.valueOf(scanner.nextInt()));
                    salvarTarefasEmArquivo(listaDeTarefas, caminhoArquivo);
                }
                case 10 -> {
                    continuar = false;
                    System.out.println("Saindo do programa...");
                    salvarTarefasEmArquivo(listaDeTarefas, caminhoArquivo);
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
        scanner.close();
    }
}