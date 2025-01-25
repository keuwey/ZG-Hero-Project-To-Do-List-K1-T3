import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ToDoList {
    static String caminhoArquivo = "tarefas.txt";
    static List<Tarefa> listaDeTarefas = carregarTarefasDeArquivo(caminhoArquivo);

    public static void salvarTarefasEmArquivo(List<Tarefa> listaDeTarefas, String caminhoArquivo) throws IOException {
        Set<String> tarefasExistentes = new HashSet<>();
        File arquivo = new File(caminhoArquivo);
        if (arquivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    tarefasExistentes.add(linha);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        List<String> novasTarefas = listaDeTarefas.stream()
                .map(ToDoList::tarefaParaLinha)
                .filter(linha -> !tarefasExistentes.contains(linha))
                .collect(Collectors.toList());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo, true))) {
            for (String linha : novasTarefas) {
                writer.write(linha);
                writer.newLine();
            }
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

    public void adicionarTarefa(Tarefa tarefa) {
        listaDeTarefas.add(tarefa);
    }

    public boolean removerTarefaPorId(int id) {
        Iterator<Tarefa> iterator = listaDeTarefas.iterator();
        while (iterator.hasNext()) {
            Tarefa tarefa = iterator.next();
            if (tarefa.getID() == id) {
                iterator.remove();
                System.out.println("Tarefa com ID " + id + " foi removida.");
                return true;
            }
        }
        System.out.println("Tarefa com ID " + id + " não encontrada.");
        return false;
    }

    public void listarPorCategoria(String categoria) {
        System.out.println("Tarefas na categoria '" + categoria + "':");
        listaDeTarefas.stream()
                .filter(tarefa -> tarefa.getCategoria().equalsIgnoreCase(categoria))
                .forEach(System.out::println);
    }

    public void listarPorStatus(String status) {
        System.out.println("Tarefas com status '" + status + "':");
        listaDeTarefas.stream()
                .filter(tarefa -> tarefa.getStatus().equalsIgnoreCase(status))
                .forEach(System.out::println);
    }

    // Lista todas as tarefas com que tenha a prioridade passada no parâmetro
    // Prioridade 1 - 3 (1 == baixa prioridade. 2 == prioridade regular. 3 == alta prioridade)
    public void listarPorPrioridade(int prioridade) {
        System.out.println("Tarefas com prioridade " + prioridade + ":");
        listaDeTarefas.stream()
                .filter(tarefa -> tarefa.getNivelDePrioridade() == prioridade)
                .forEach(System.out::println);
    }

    public void listarTodasTarefas() {
        System.out.println("Tarefas na lista:");
        listaDeTarefas.forEach(System.out::println);
    }

    public void listarPorPrioridadeCrescente() {
        Collections.sort(listaDeTarefas, Comparator.comparingInt(Tarefa::getNivelDePrioridade));
        System.out.println("Tarefas por prioridade (crescente):");
        listarTodasTarefas();
    }

    public void listarPorPrioridadeDecrescente() {
        Collections.sort(listaDeTarefas, Comparator.comparingInt(Tarefa::getNivelDePrioridade).reversed());
        System.out.println("Tarefas por prioridade (decrescente):");
        listarTodasTarefas();
    }

    public void listarPorData(LocalDate data) {
        System.out.println("Tarefas com data de término " + data + ":");
        listaDeTarefas.stream()
                .filter(tarefa -> tarefa.getDataDeTermino().isEqual(data))
                .forEach(System.out::println);
    }

    public static void main(String[] args) throws IOException {

        ToDoList toDoList = new ToDoList();

        Tarefa tarefa1 = new Tarefa("Comprar frutas", "Comprar banana, maçã, uva, pêra, abacaxi e mamão no supermercado", "Compras", "To Do", LocalDate.of(2025, 1, 27), 1, 1);
        tarefa1.setNome("Fugir daqui");

        Tarefa tarefa2 = new Tarefa("Estudar java", "Estudar mais sobre threads em java", "Estudos", "Doing", LocalDate.of(2025, 1, 31), 3, 2);

        Tarefa tarefa3 = new Tarefa("Estudar matemática", "Estudar funções, cálculo, integral etc", "Estudos", "To Do", LocalDate.of(2025, 5, 1), 3, 3);

        toDoList.adicionarTarefa(tarefa1);
        toDoList.adicionarTarefa(tarefa2);
        toDoList.adicionarTarefa(tarefa3);

        toDoList.listarPorCategoria("Compras");
        System.out.println();
        toDoList.listarPorStatus("Doing");
        System.out.println();
        toDoList.listarPorPrioridade(3);
        toDoList.listarPorData(LocalDate.of(2025, 1, 31));
        System.out.println();

        toDoList.listarPorPrioridadeCrescente();
        toDoList.listarPorPrioridadeDecrescente();

        System.out.println();

        salvarTarefasEmArquivo(listaDeTarefas, caminhoArquivo);
        toDoList.listarTodasTarefas();
    }

}