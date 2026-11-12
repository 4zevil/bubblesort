package org.example;

import java.io.IOException;

/**
 * Ponto de entrada principal da aplicação "Estrutura de Dados S.A.".
 * <p>
 * Carrega a base de dados de empresas a partir do arquivo TXT,
 * aplica a ordenação por valor de mercado em ordem DECRESCENTE (do maior para o menor)
 * via algoritmo Bubble Sort (com suporte a alternância simples entre a versão clássica
 * e a versão paralela), e grava o resultado no arquivo final de saída.
 * </p>
 *
 * @author Gabriel Lira Figueira
 * @author Eduarda Santana Santos
 * @author João Luís Aragão Rocha
 * @version 1.0
 */
public class Main {

    /** Caminho padrão do arquivo de entrada fornecido pelo exercício. */
    private static final String ARQUIVO_ENTRADA = "Empresas_BubbleSort.txt";

    /** Caminho do arquivo de saída gerado com os registros ordenados. */
    private static final String ARQUIVO_SAIDA = "Empresas_Ordenadas.txt";

    /**
     * Executa o fluxo completo do sistema:
     * <ol>
     *   <li>Leitura do arquivo de empresas.</li>
     *   <li>Ordenação decrescente por valor de mercado.</li>
     *   <li>Persistência no arquivo de destino.</li>
     * </ol>
     *
     * @param args argumentos de linha de comando (opcional: caminho de entrada e saída)
     */
    public static void main(String[] args) {
        String arquivoOrigem = (args.length > 0) ? args[0] : ARQUIVO_ENTRADA;
        String arquivoDestino = (args.length > 1) ? args[1] : ARQUIVO_SAIDA;

        System.out.println("================================================================================");
        System.out.println("  ESTRUTURA DE DADOS S.A. - CLASSIFICAÇÃO DE VALOR DE MERCADO");
        System.out.println("================================================================================");
        System.out.println("Arquivo de entrada : " + arquivoOrigem);
        System.out.println("Arquivo de saída   : " + arquivoDestino);

        try {
            // 1. LEITURA DOS DADOS
            System.out.print("\n[1/3] Lendo registros do arquivo de entrada... ");
            long tempoInicioLeitura = System.currentTimeMillis();
            Registro[] registros = ArquivoServico.lerArquivo(arquivoOrigem);
            long tempoFimLeitura = System.currentTimeMillis();
            System.out.printf("Concluído! (%d empresas carregadas em %d ms)%n", 
                    registros.length, (tempoFimLeitura - tempoInicioLeitura));

            if (registros.length == 0) {
                System.out.println("Aviso: O arquivo está vazio ou sem registros válidos.");
                return;
            }

            // 2. ESCOLHA DA ESTRATÉGIA DE ORDENAÇÃO (Fácil substituição com POO / Polimorfismo)
            // Caso queira usar a versão clássica puramente sequencial, basta descomentar a linha abaixo:
            // Ordenador ordenador = new BubbleSortClassico();
            Ordenador ordenador = new BubbleSortParalelo();

            System.out.printf("%n[2/3] Ordenando registros com [%s] (Maior para Menor)...%n", ordenador.getNomeEstrategia());
            long tempoInicioOrdenacao = System.currentTimeMillis();
            ordenador.ordenarDecrescente(registros);
            long tempoFimOrdenacao = System.currentTimeMillis();
            System.out.printf("Ordenação concluída com sucesso em %d ms!%n", (tempoFimOrdenacao - tempoInicioOrdenacao));

            // 3. ESCRITA NO ARQUIVO DE SAÍDA
            System.out.printf("%n[3/3] Gravando registros ordenados em '%s'... ", arquivoDestino);
            long tempoInicioEscrita = System.currentTimeMillis();
            ArquivoServico.gravarArquivo(registros, arquivoDestino);
            long tempoFimEscrita = System.currentTimeMillis();
            System.out.printf("Concluído em %d ms!%n", (tempoFimEscrita - tempoInicioEscrita));

            // 4. EXIBIÇÃO DE AMOSTRA DOS RESULTADOS NO CONSOLE
            exibirAmostraConsole(registros);

            System.out.println("\n>>> PROCESSO CONCLUÍDO COM SUCESSO! <<<");
            System.out.println("================================================================================");

        } catch (IOException e) {
            System.err.println("\n[ERRO DE I/O]: Falha ao processar arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("\n[ERRO DE FORMATO]: Valor numérico inválido encontrado no arquivo.");
        }
    }

    /**
     * Exibe no terminal as empresas de maior valor (topo) e menor valor (base)
     * para validação imediata do usuário.
     *
     * @param registros Vetor de registros já ordenados
     */
    private static void exibirAmostraConsole(Registro[] registros) {
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("  AMOSTRA DOS REGISTROS ORDENADOS (TOP EMPRESAS DE MAIOR VALOR)");
        System.out.println("--------------------------------------------------------------------------------");
        int limiteAmostra = Math.min(5, registros.length);
        for (int i = 0; i < limiteAmostra; i++) {
            System.out.printf("  #%d | %s%n", (i + 1), registros[i]);
        }

        if (registros.length > 5) {
            System.out.println("  ...");
            System.out.printf("  #%d (Menor Valor) | %s%n", registros.length, registros[registros.length - 1]);
        }
        System.out.println("--------------------------------------------------------------------------------");
    }
}
