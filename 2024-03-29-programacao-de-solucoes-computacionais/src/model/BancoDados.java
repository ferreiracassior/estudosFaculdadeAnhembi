package model;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import model.classes.Evento;
import model.classes.Usuario;

public class BancoDados {
    
    private static void validarSeArquivoExisteSenaoCria(String caminho) {
        File f = new File(caminho);
        if(!f.exists()) { 
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<String[]> lerDeArquivo(String arquivo) throws IOException {
        validarSeArquivoExisteSenaoCria("2024-03-29-programacao-de-solucoes-computacionais\\" + arquivo);
        ArrayList<String[]> dados = new ArrayList<String[]>();

        Scanner scanner = new Scanner(new File("2024-03-29-programacao-de-solucoes-computacionais\\" + arquivo));
        scanner.useDelimiter("\n--");

        while (scanner.hasNext()) {
            String[] linha = scanner.next().split(";;;");
            dados.add(linha);
        }
        scanner.close();
        return dados;
    }

    public static void escreverEmArquivo(String[] campos, String arquivo) throws IOException {
        validarSeArquivoExisteSenaoCria("2024-03-29-programacao-de-solucoes-computacionais\\" + arquivo);
        FileWriter escritor = new FileWriter("2024-03-29-programacao-de-solucoes-computacionais\\" + arquivo, true);
        escritor.write("\n--");
        int count = 1;
        for (String valor : campos) {
            escritor.write(valor + (count != campos.length ? ";;;" : ""));
            count++;
        }
        escritor.close();
    }

    public static void limparArquivo(String arquivo) throws IOException {
        validarSeArquivoExisteSenaoCria("2024-03-29-programacao-de-solucoes-computacionais\\" + arquivo);
        FileWriter escritor = new FileWriter("2024-03-29-programacao-de-solucoes-computacionais\\" + arquivo, false);
        escritor.write("");
        escritor.close();
    }

    
    public static ArrayList<Usuario> convDadosBancoParaListaUsuarios(ArrayList<String[]> dados) {
        ArrayList<Usuario> retorno = new ArrayList<Usuario>();
        
        for (String[] dado : dados) {
            retorno.add(
                new Usuario(dado[0], dado[1], dado[2])
            );
        }
        return retorno;
    }

    public static ArrayList<Evento> convDadosBancoParaListaEventos(ArrayList<String[]> dados) throws ParseException {
        ArrayList<Evento> retorno = new ArrayList<Evento>();
        
        for (String[] dado : dados) {           
            Evento evento = new Evento(dado[0], dado[1], dado[2], new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dado[3]), dado[4]);

            if (dado.length > 5) {
                String[] participantes;
                participantes = dado[5].split(";");
                for (String participante : participantes) {
                    evento.incluirCpfParticipanteEvento(participante);
                }
            }
            
            retorno.add(evento);
        }
        return retorno;
    }

}
