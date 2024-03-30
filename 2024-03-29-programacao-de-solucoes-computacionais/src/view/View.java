package view;

import java.util.Scanner;

public class View {
    public static void exibe(String mensagem) {
        System.out.println(mensagem);
    }

    public static void exibeErro(String mensagem) {
        System.out.println("ERRO: " + mensagem);
    }

    public static String leia(String mensagem) {
        System.out.println(mensagem);
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        return input;
    }
}
