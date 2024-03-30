package model;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import model.classes.*;
import view.View;
import controller.Controller;


public class GerenciadorEventos {
    
    public static void cadastrarEvento() throws IOException, ParseException {

        java.util.Date horario = Date.from(java.time.Instant.now());
        boolean dataValida = false;
        boolean categoriaValida = false;

        do {
            try {
                String horarioStr = View.leia("=> informe a data e horário do evento no formato: DIA/MES/ANO HH:MM");
                horario = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(horarioStr);
                dataValida = true;
            } catch (Exception e) {
                View.exibeErro("=> data e hora inválida, digite novamente");
            }
        } while (dataValida == false);

        String categoria = "";

        do {
            categoria = View.leia("=> informe a categoria (1 - festas, 2 - eventos esportivos, 3- shows, 4- formaturas):");
            if (categoria.equals("1") || categoria.equals("2") || categoria.equals("3") || categoria.equals("4")) {
                categoriaValida = true;
            } else {
                View.exibeErro("=> categoria inválida, digite novamente");
            }
        } while (categoriaValida == false);

        Evento novoEvento = new Evento(
            View.leia("=> informe o nome:"),
            View.leia("=> informe a endereço:"),
            categoria,
            horario,
            View.leia("=> informe a descrição:")
        );

        salvarEvento(novoEvento);
        View.exibe("=> evento cadastrado com sucesso!");
        Controller.executarMenu();
    }

    private static void salvarEvento(Evento evento) throws IOException {
        String participantesString = "";
        for (String cpf : evento.getParticipantes()) {
            participantesString += cpf + ";";
        }
        
        String[] campos = {
            evento.getNome(),
            evento.getEndereco(),
            evento.getCategoria(),
            new SimpleDateFormat("dd/MM/yyyy HH:mm").format(evento.getHorario()),
            evento.getDescricao(),
            participantesString
        };
        BancoDados.escreverEmArquivo(campos, "eventos.data");
    }

    private static int ListarEventosEObtemNumeroEventoDesejado(boolean inscricao, ArrayList<Evento> eventos) {
        
        int count = 1;
        boolean opcaoValida = false;
        int opcaoInt = 0;

        while (!opcaoValida) {
            try {
                View.exibe("=> digite o numero do evento que deseja " + (inscricao ? "realizar inscrição" : "cancelar inscriçção"));
                for (Evento e : eventos) {
                    View.exibe(count + " - " + e.getNome());
                    count++;
                }

                String opcao = View.leia("0 - retornar ao menu");
                opcaoInt = Integer.parseInt(opcao);

                if (opcaoInt > eventos.size() || opcaoInt < 0) {
                    View.exibeErro("=> opção inválida, digite novamente");
                } else {
                    opcaoValida = true;
                } 
            
            } catch (Exception e) {
                View.exibeErro("=> opção inválida, digite novamente");
            }
        }

        return opcaoInt;
    }

    private static void ListarEventos(List<Evento> eventos) {
        for (Evento e : eventos) {
            View.exibe("-Evento: " + e.getNome() + " >> Data/Hora: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(e.getHorario()) + " >> total inscritos: " + e.getParticipantes().size());
        }
    }

    public static void ListarTodosEventosCpf() throws IOException, ParseException {
        ArrayList<String[]> dadosBanco = BancoDados.lerDeArquivo("eventos.data");
        ArrayList<Evento> eventos = BancoDados.convDadosBancoParaListaEventos(dadosBanco);
        List<Evento> eventosList = eventos;

        String cpf = GerenciadorUsuarios.lerCPFValidoNaBaseDados(); 

        if (cpf == null) {
            Controller.executarMenu();
            return;
        }
        eventosList.removeIf(e -> !e.getParticipantes().contains(cpf));

        if (eventosList.size() == 0) {
            View.exibe("=> cpf não está inscrito em nenhum evento");
            View.leia("pressione algo para continuar...");
            Controller.executarMenu();
            return;
        }

        View.exibe("=> eventos cadastrados para o cpf " + cpf + ":");
        Collections.sort(eventosList, (e1, e2) -> e1.getHorario().compareTo(e2.getHorario()));
        ListarEventos(eventosList);
        View.leia("pressione algo para continuar...");
        Controller.executarMenu();
    }

    public static void ListarEventoQueAindaOcorrremEsteMes() throws IOException, ParseException {
        ArrayList<String[]> dadosBanco = BancoDados.lerDeArquivo("eventos.data");
        ArrayList<Evento> eventos = BancoDados.convDadosBancoParaListaEventos(dadosBanco);
        List<Evento> eventosList = eventos;

        Calendar calNow = Calendar.getInstance();
        calNow.setTime(new Date());

        eventosList.removeIf(e -> {
            Calendar calEvento = Calendar.getInstance();
            calEvento.setTime(e.getHorario());

            return !(
                        (calEvento.get(Calendar.MONTH) == calNow.get(Calendar.MONTH)) &&
                        (calEvento.get(Calendar.YEAR) == calNow.get(Calendar.YEAR)) &&
                        (e.getHorario().after(new Date()))
                    );
        });

        if (eventosList.size() == 0) {
            View.exibe("=> não há eventos para este mês e ano");
            View.leia("pressione algo para continuar...");
            Controller.executarMenu();
            return;
        }
        
        View.exibe("=> eventos que ainda ocorrem este mes:");
        Collections.sort(eventosList, (e1, e2) -> e1.getHorario().compareTo(e2.getHorario()));
        ListarEventos(eventosList);
        View.leia("pressione algo para continuar...");
        Controller.executarMenu();
    }

    public static void ListarQueJaOcorreram() throws IOException, ParseException {
        ArrayList<String[]> dadosBanco = BancoDados.lerDeArquivo("eventos.data");
        ArrayList<Evento> eventos = BancoDados.convDadosBancoParaListaEventos(dadosBanco);
        List<Evento> eventosList = eventos;

        eventosList.removeIf(e -> !e.getHorario().before(new Date()));

        if (eventosList.size() == 0) {
            View.exibe("=> não há eventos no histórico");
            View.leia("pressione algo para continuar...");
            Controller.executarMenu();
            return;
        }
        
        View.exibe("=> eventos que já ocorreram:");
        Collections.sort(eventosList, (e1, e2) -> e1.getHorario().compareTo(e2.getHorario()));
        ListarEventos(eventosList);
        View.leia("pressione algo para continuar...");
        Controller.executarMenu();
    }


    public static void inscreverCpfNoEvento() throws IOException, ParseException {

        ArrayList<String[]> dadosBanco = BancoDados.lerDeArquivo("eventos.data");
        ArrayList<Evento> eventos = BancoDados.convDadosBancoParaListaEventos(dadosBanco);
       
        int numeroEvento = ListarEventosEObtemNumeroEventoDesejado(true, eventos);

        if (numeroEvento == 0) {
            Controller.executarMenu();
            return;
        }

        String cpf = GerenciadorUsuarios.lerCPFValidoNaBaseDados(); 

        if (cpf == null) {
            Controller.executarMenu();
            return;
        }

        if (eventos.get(numeroEvento - 1).getParticipantes().contains(cpf)) {
            View.exibeErro("=> cpf " + cpf + " já está inscrito neste evento");
            Controller.executarMenu();
            return;
        }

        eventos.get(numeroEvento - 1).incluirCpfParticipanteEvento(cpf);

        BancoDados.limparArquivo("eventos.data");
        for (Evento e : eventos) {
            salvarEvento(e);
        }

        View.exibe("=> cpf inscrito com sucesso!");
        Controller.executarMenu();
    }

    public static void desinscreverCpfNoEvento() throws IOException, ParseException {

        ArrayList<String[]> dadosBanco = BancoDados.lerDeArquivo("eventos.data");
        ArrayList<Evento> eventos = BancoDados.convDadosBancoParaListaEventos(dadosBanco);
       
        int numeroEvento = ListarEventosEObtemNumeroEventoDesejado(false, eventos);

        if (numeroEvento == 0) {
            Controller.executarMenu();
            return;
        }

        String cpf = GerenciadorUsuarios.lerCPFValidoNaBaseDados(); 

        if (cpf == null) {
            Controller.executarMenu();
            return;
        }

        if (eventos.get(numeroEvento - 1).getParticipantes().contains(cpf)) {
            eventos.get(numeroEvento - 1).removerCpfParticipanteEvento(cpf);
            
            BancoDados.limparArquivo("eventos.data");
            for (Evento e : eventos) {
                salvarEvento(e);
            }

            View.exibeErro("=> cpf " + cpf + " removido do evento com sucesso");
            Controller.executarMenu();
            return;
        } else {
            View.exibeErro("=> cpf " + cpf + " não está inscrito neste evento");
            Controller.executarMenu();
            return;
        }
    }


}
