package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitário responsável pela leitura e escrita de arquivos de registros no formato TXT.
 * <p>
 * Centraliza as operações de I/O em conformidade com o princípio DRY (Don't Repeat Yourself).
 * </p>
 *
 * @author Gabriel Lira Figueira
 * @author Eduarda Santana Santos
 * @author João Luís Aragão Rocha
 * @version 1.0
 */
public class ArquivoServico {

    /**
     * Lê um arquivo TXT contendo empresas separadas pelo delimitador '|'
     * e converte cada linha válida em um vetor de {@link Registro}.
     *
     * @param caminhoArquivo Caminho relativo ou absoluto do arquivo de entrada
     * @return Vetor de registros de empresas carregados na memória
     * @throws IOException Se ocorrer erro na leitura do arquivo
     */
    public static Registro[] lerArquivo(String caminhoArquivo) throws IOException {
        List<Registro> listaTemporaria = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) {
                    continue; // Ignora linhas em branco
                }

                String[] campos = linha.split("\\|");
                if (campos.length != 3) {
                    continue; // Ignora linhas fora do padrão de 3 colunas
                }

                String nomeEmpresa = campos[0].trim();
                String inscricao = campos[1].trim();
                double valorMercado = Double.parseDouble(campos[2].trim());

                listaTemporaria.add(new Registro(nomeEmpresa, inscricao, valorMercado));
            }
        }

        // Converte para array primitivo de objetos (muito mais rápido para troca/swap de posições)
        return listaTemporaria.toArray(new Registro[0]);
    }

    /**
     * Grava um vetor de registros em um novo arquivo TXT, linha por linha.
     *
     * @param registros      Vetor de registros a serem salvos
     * @param caminhoDestino Caminho do arquivo TXT a ser criado
     * @throws IOException Se ocorrer erro na escrita do arquivo
     */
    public static void gravarArquivo(Registro[] registros, String caminhoDestino) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoDestino))) {
            for (Registro r : registros) {
                bw.write(r.toString());
                bw.newLine();
            }
        }
    }
}
