import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Tarefa {
    private String Nome, Descricao, Categoria, Status;
    private LocalDate DataDeTermino;
    private int NivelDePrioridade;
    private final int ID;


    public Tarefa(String nome,
                  String descricao,
                  String categoria,
                  String status,
                  LocalDate dataDeTermino,
                  int nivelDePrioridade,
                  int id) {

        Nome = nome;
        Descricao = descricao;
        Categoria = categoria;
        Status = status;
        DataDeTermino = dataDeTermino;
        NivelDePrioridade = nivelDePrioridade;
        ID = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        if (nome != null && !nome.isEmpty()) {
            Nome = nome;
        }
        else {
            System.out.println("O nome não pode ser vazio");
        }
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao != null && !descricao.isEmpty()) {
            Descricao = descricao;
        }
        else {
            System.out.println("A descrição não pode ser vazia");
        }
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria != null && !categoria.isEmpty()) {
            Categoria = categoria;
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

    public LocalDate getDataDeTermino() {
        return DataDeTermino;
    }

    public void setDataDeTermino(LocalDate dataDeTermino) {
        if (dataDeTermino.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data de conclusão não pode estar no passado!");
        }
        DataDeTermino = dataDeTermino;
        System.out.println("Data de término atualizada para: " + dataDeTermino);
    }

    public int getNivelDePrioridade() {
        return NivelDePrioridade;
    }

    public void setNivelDePrioridade(int nivelDePrioridade) {
        NivelDePrioridade = nivelDePrioridade;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return
                "Nome = '" + Nome + '\'' +
                        ", Descricao = '" + Descricao + '\'' +
                        ", Categoria = '" + Categoria + '\'' +
                        ", Status = '" + Status + '\'' +
                        ", DataDeTermino = " + (DataDeTermino != null ? DataDeTermino.format(formatter) : "Data não definida")
                        + ", NivelDePrioridade = " + NivelDePrioridade;
    }
}
