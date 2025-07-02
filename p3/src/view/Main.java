package view;

import controller.ArquivosController;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String caminho = "C:\\TEMP";
        String nomeArquivo = "hol.json";

        ArquivosController controle = new ArquivosController();

        try {
            controle.readFile(caminho, nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}
