package model.classes;

public class Usuario {
    private String nome;
    private String cidade;
    private String cpf;

    public Usuario(String _nome, String _cidade, String _cpf) {
        nome = _nome;
        cidade = _cidade;
        cpf = _cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getCidade() {
        return cidade;
    }

    public String getCpf() {
        return cpf;
    }
}




