package model.classes;

import java.util.ArrayList;

public class Evento {
    private String nome;
    private String endereco;
    private String categoria;
    private java.util.Date horario;
    private String descricao;
    private ArrayList<String> participantes;

    public Evento(String _nome, String _endereco, String _categoria, java.util.Date _horario, String _descricao) {
        nome = _nome;
        endereco = _endereco;
        categoria = _categoria;
        horario = _horario;
        descricao = _descricao;
        participantes = new ArrayList<String>();
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getCategoria() {
        return categoria;
    }

    public java.util.Date getHorario() {
        return horario;
    }

    public String getDescricao() {
        return descricao;
    }

    public ArrayList<String> getParticipantes() {
        return participantes;
    }

    public void incluirCpfParticipanteEvento(String cpfParticipante) {
        participantes.add(cpfParticipante);
    }

    public void removerCpfParticipanteEvento(String cpfParticipante) {
        participantes.remove(cpfParticipante);
    }
}




