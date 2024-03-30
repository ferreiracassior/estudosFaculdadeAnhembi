package controller;
import view.*;

import java.io.IOException;
import java.text.ParseException;

import model.*;

public class Controller {

    public static void iniciar() throws IOException, ParseException {
        View.exibe("=> bem vindo ao gerenciador de eventos!");
        executarMenu();
    }

    private static void finalizar() {
        View.exibe("=> obrigado por usar o gerenciador de eventos!");
        System.exit(0);
    }

    public static void executarMenu() throws IOException, ParseException {
        String opSelecionada = View.leia(
            "\n MENU PRINCIPAL -> escolha uma opção:\n" + 
            "  1 - Cadastrar usuário + cpf\n" + 
            "  2 - Cadastrar evento\n" +
            "  3 - Inscrever-se em um evento\n" +
            "  4 - Cancelar Inscrição em Evento em um evento\n" +
            "  5 - Listar todos Eventos de um cpf\n" +
            "  6 - Listar Eventos que ocorrerão neste mes \n" +
            "  7 - Listar Eventos que já ocorreram\n" +
            "  8 - Sair\n"
        );  

        switch (opSelecionada) {
            case "1":
                GerenciadorUsuarios.cadastrarUsuario();
                break;
            case "2":
                GerenciadorEventos.cadastrarEvento();
                break;
            case "3":
                GerenciadorEventos.inscreverCpfNoEvento();
                break;
            case "4":
                GerenciadorEventos.desinscreverCpfNoEvento();
                break;
            case "5":
                GerenciadorEventos.ListarTodosEventosCpf();
                break;
            case "6":
                GerenciadorEventos.ListarEventoQueAindaOcorrremEsteMes();
                break;
            case "7":
                GerenciadorEventos.ListarQueJaOcorreram();
                break;
            case "8":
                finalizar();
                break;
            default:
                View.exibeErro("=> opção '" + opSelecionada + "' inválida, tente novamente");
                executarMenu();
                break;
        }   
    }
}
