package model;
import java.io.IOException;
import java.text.ParseException;

import model.classes.*;
import view.View;
import java.util.ArrayList;

import controller.Controller;


public class GerenciadorUsuarios {
    
    public static void cadastrarUsuario() throws IOException, ParseException {
        
        Usuario novoUsuario = new Usuario(
            View.leia("=> informe o nome:"),
            View.leia("=> informe a cidade:"),
            View.leia("=> informe o cpf:")
        );

        ArrayList<String[]> dadosBanco = BancoDados.lerDeArquivo("usuarios.data");
        ArrayList<Usuario> usuariosCadastrados = BancoDados.convDadosBancoParaListaUsuarios(dadosBanco);
       
        for (Usuario usuario : usuariosCadastrados) {
            if (usuario.getCpf().equals(novoUsuario.getCpf())) {
                View.exibeErro("=> cpf já cadastrado, retornando ao menu...");
                Controller.executarMenu();
                return;
            }
        }

        salvarUsuario(novoUsuario);
        View.exibe("=> usuário cadastrado com sucesso!");
        Controller.executarMenu();
    }


    private static void salvarUsuario(Usuario usuario) throws IOException {
        String[] campos = {
            usuario.getNome(),
            usuario.getCidade(),
            usuario.getCpf()
        };
        BancoDados.escreverEmArquivo(campos, "usuarios.data");
    }

    public static Usuario buscarCpfNaListaUsuarios(String cpf) throws IOException {
        ArrayList<String[]> dadosBanco = BancoDados.lerDeArquivo("usuarios.data");
        ArrayList<Usuario> usuarios = BancoDados.convDadosBancoParaListaUsuarios(dadosBanco);
        
        Usuario usuarioEncontrado = usuarios.stream().filter(u -> u.getCpf().equals(cpf)).findFirst().orElse(null);

        return usuarioEncontrado;
    }

    public static String lerCPFValidoNaBaseDados() throws IOException, ParseException {
        String cpfLocalizado = null;
        String cpfBuscar = "";
        boolean continuarProcurando = true;

        while (continuarProcurando) {
            cpfBuscar = View.leia("=> digite um numero do cpf existente na base de dados ou '0' para sair: ");
            Usuario usuario = GerenciadorUsuarios.buscarCpfNaListaUsuarios(cpfBuscar);
    
            if (usuario != null) {
                cpfLocalizado = cpfBuscar;
                continuarProcurando = false;
            } else {
                if (cpfBuscar.equals("0")) {
                    continuarProcurando = false;
                } else {
                    View.exibe("=> cpf não encontrado, tente novamente");
                }
            }
        }

        return cpfLocalizado;
    }
}
