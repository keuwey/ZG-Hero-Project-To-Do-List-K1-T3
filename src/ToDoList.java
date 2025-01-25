import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ToDoList {
    List<Tarefa> listaDeTarefas = new ArrayList<>();

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

    public void listarPorPrioridade(int prioridade) {
        System.out.println("Tarefas com prioridade " + prioridade + ":");
        listaDeTarefas.stream()
                .filter(tarefa -> tarefa.getNivelDePrioridade() == prioridade)
                .forEach(System.out::println);
    }

    public void listarPorData(LocalDate data) {
        System.out.println("Tarefas com data de término " + data + ":");
        listaDeTarefas.stream()
                .filter(tarefa -> tarefa.getDataDeTermino().isEqual(data))
                .forEach(System.out::println);
    }

    public static void main(String[] args) {

        ToDoList toDoList = new ToDoList();

        Tarefa tarefa1 = new Tarefa("Comprar frutas", "Comprar banana, maçã, uva, pêra, abacaxi e mamão no supermercado", "Compras", "To Do", LocalDate.of(2025, 1, 27), 1, 1);
        tarefa1.setNome("Fugir daqui");

        Tarefa tarefa2 = new Tarefa("Estudar java", "Estudar mais sobre threads em java", "Estudos", "Doing", LocalDate.of(2025, 1, 31), 5, 2);

        Tarefa tarefa3 = new Tarefa("Estudar matemática", "Estudar funções, cálculo, integral etc", "Estudos", "To Do", LocalDate.of(2025, 5, 1), 5, 3);

        toDoList.adicionarTarefa(tarefa1);
        toDoList.adicionarTarefa(tarefa2);
        toDoList.adicionarTarefa(tarefa3);

        toDoList.listarPorCategoria("Compras");
        toDoList.listarPorStatus("Doing");
        toDoList.listarPorPrioridade(5);
        toDoList.listarPorData(LocalDate.of(2025, 1, 31));

        System.out.println();

        for (Tarefa tarefa : toDoList.listaDeTarefas) {
            System.out.println(tarefa);
        }
        toDoList.removerTarefaPorId(3);
        for (Tarefa tarefa : toDoList.listaDeTarefas) {
            System.out.println(tarefa);
        }

    }
}