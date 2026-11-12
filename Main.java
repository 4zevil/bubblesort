package org.example;

import java.io.IOException;

/**
 * Ponto de entrada principal da aplicação "Estrutura de Dados S.A.".
 * <p>
 * Carrega a base de dados de empresas a partir do arquivo TXT,
 * calcula previamente uma estimativa científica de tempo de ordenação,
 * aplica a ordenação por valor de mercado em ordem DECRESCENTE (do maior para o menor)
 * via algoritmo Bubble Sort (com suporte a alternância simples entre a versão clássica
 * e a versão paralela), e grava o resultado no arquivo final de saída.
 * </p>
 *
 * @author Gabriel Lira Figueira
 * @author Eduarda Santana Santos
 * @author João Luís Aragão Rocha
 * @version 1.1
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
     *   <li>Estimativa prévia de tempo de ordenação (baseada em benchmark de CPU).</li>
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

            // 2. ESCOLHA DA ESTRATÉGIA DE ORDENAÇÃO (Polimorfismo com Padrão Strategy)
            // Caso queira usar a versão clássica pura, basta descomentar:
            // Ordenador ordenador = new BubbleSortClassico();
            Ordenador ordenador = new BubbleSortParalelo();

            // ESTIMATIVA DE TEMPO INTELIGENTE ANTES DE TRAVAR A MÁQUINA
            exibirEstimativaTempo(registros.length);

            // 3. EXECUÇÃO DA ORDENAÇÃO
            System.out.printf("[2/3] Ordenando registros com [%s] (Maior para Menor)...%n", ordenador.getNomeEstrategia());
            long tempoInicioOrdenacao = System.currentTimeMillis();
            ordenador.ordenarDecrescente(registros);
            long tempoFimOrdenacao = System.currentTimeMillis();
            System.out.printf("Ordenação concluída com sucesso em %d ms!%n", (tempoFimOrdenacao - tempoInicioOrdenacao));

            // 4. ESCRITA NO ARQUIVO DE SAÍDA
            System.out.printf("%n[3/3] Gravando registros ordenados em '%s'... ", arquivoDestino);
            long tempoInicioEscrita = System.currentTimeMillis();
            ArquivoServico.gravarArquivo(registros, arquivoDestino);
            long tempoFimEscrita = System.currentTimeMillis();
            System.out.printf("Concluído em %d ms!%n", (tempoFimEscrita - tempoInicioEscrita));

            // 5. EXIBIÇÃO DE AMOSTRA DOS RESULTADOS NO CONSOLE
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
     * Calcula e exibe uma estimativa do tempo total necessário para o Bubble Sort,
     * rodando um micro-benchmark em lote reduzido para medir a velocidade real da CPU.
     *
     * @param n Quantidade de registros carregados
     */
    private static void exibirEstimativaTempo(int n) {
        if (n <= 500) {
            return; // Quantidades pequenas executam instantaneamente
        }

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("  ESTIMATIVA MATEMÁTICA DE PROCESSAMENTO (Complexidade O(N²))");
        System.out.println("--------------------------------------------------------------------------------");

        // Total teórico de operações no Bubble Sort: (N * (N - 1)) / 2
        double totalOperacoes = ((double) n * (n - 1)) / 2.0;
        System.out.printf("  Total de comparações estimadas: %,.0f operações%n", totalOperacoes);

        // Medição de velocidade em tempo real: roda um mini-teste de 2.000 itens para aferir a CPU atual
        int amostra = 2000;
        Registro[] miniVetor = new Registro[amostra];
        for (int i = 0; i < amostra; i++) {
            miniVetor[i] = new Registro("E" + i, "0", amostra - i);
        }

        long t0 = System.nanoTime();
        new BubbleSortClassico().ordenarDecrescente(miniVetor);
        long t1 = System.nanoTime();

        double opsAmostra = ((double) amostra * (amostra - 1)) / 2.0;
        double segundosAmostra = (t1 - t0) / 1_000_000_000.0;
        double velocidadePorSegundo = opsAmostra / Math.max(0.001, segundosAmostra);

        // Tempo estimado total em segundos
        double segundosTotais = totalOperacoes / velocidadePorSegundo;

        if (segundosTotais < 1.0) {
            System.out.println("  Tempo estimado: Menos de 1 segundo.");
        } else if (segundosTotais < 60.0) {
            System.out.printf("  Tempo estimado: ~%.1f segundos.%n", segundosTotais);
        } else if (segundosTotais < 3600.0) {
            System.out.printf("  Tempo estimado: ~%.1f minutos.%n", (segundosTotais / 60.0));
        } else {
            double horas = segundosTotais / 3600.0;
            System.out.printf("  Tempo estimado: ~%.1f HORAS (%.1f dias de processamento contínuo)%n", horas, (horas / 24.0));
            System.out.println("  [AVISO]: O Bubble Sort O(N²) requer centenas de bilhões de passos para esse volume.");
        }
        System.out.println("--------------------------------------------------------------------------------\n");
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
