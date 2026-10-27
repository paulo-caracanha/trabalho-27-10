package com.exemplo.produtos.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppLogger {

    private static final String ARQUIVO = "app.log";
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static volatile AppLogger instancia;

    private AppLogger() {
    }

    public static AppLogger getInstance() {
        if (instancia == null) {
            synchronized (AppLogger.class) {
                if (instancia == null) {
                    instancia = new AppLogger();
                }
            }
        }
        return instancia;
    }

    public void debug(String mensagem) {
        registrar("DEBUG", mensagem);
    }

    public void info(String mensagem) {
        registrar("INFO", mensagem);
    }

    public void erro(String mensagem) {
        registrar("ERRO", mensagem);
    }

    private synchronized void registrar(String nivel, String mensagem) {
        String linha = LocalDateTime.now().format(FORMATO) + " [" + nivel + "] " + mensagem;
        System.out.println(linha);
        try (FileWriter escritor = new FileWriter(ARQUIVO, true)) {
            escritor.write(linha + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("Nao foi possivel escrever em " + ARQUIVO + ": " + e.getMessage());
        }
    }
}
