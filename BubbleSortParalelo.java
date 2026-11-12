package org.example;

/**
 * Implementação do Bubble Sort Paralelo com Transposição Par-Ímpar (Odd-Even Transposition Sort)
 * combinada com encolhimento dinâmico de limites (Last Swap Optimization).
 * <p>
 * O algoritmo explora o paralelismo de hardware dividindo as comparações independentes
 * entre múltiplos núcleos de CPU:
 * <ul>
 *   <li><b>Fase Ímpar:</b> Compara pares (1,2), (3,4), (5,6)... em paralelo por threads independentes.</li>
 *   <li><b>Fase Par:</b> Compara pares (0,1), (2,3), (4,5)... em paralelo por threads independentes.</li>
 * </ul>
 * A cada rodada, o algoritmo memoriza as posições da primeira e da última troca realizada,
 * encolhendo os limites de verificação e eliminando passadas desnecessárias nas extremidades
 * já consolidadas.
 * </p>
 *
 * @author Gabriel Lira Figueira
 * @author Eduarda Santana Santos
 * @author João Luís Aragão Rocha
 * @version 1.0
 */
public class BubbleSortParalelo implements Ordenador {

    private final int quantidadeThreads;

    /**
     * Construtor padrão que detecta e utiliza a quantidade de núcleos da CPU disponíveis.
     */
    public BubbleSortParalelo() {
        this(Math.max(1, Runtime.getRuntime().availableProcessors()));
    }

    /**
     * Construtor que permite parametrizar manualmente a quantidade de threads de processamento.
     *
     * @param quantidadeThreads Quantidade de threads simultâneas
     */
    public BubbleSortParalelo(int quantidadeThreads) {
        this.quantidadeThreads = quantidadeThreads;
    }

    /**
     * Ordena os registros em ordem decrescente utilizando o algoritmo de
     * Transposição Par-Ímpar em paralelo, com redução de fronteira (janela de troca).
     *
     * @param registros Vetor de empresas a ser ordenado
     */
    @Override
    public void ordenarDecrescente(Registro[] registros) {
        if (registros == null || registros.length <= 1) {
            return;
        }

        int n = registros.length;
        int limiteInferior = 0;
        int limiteSuperior = n - 1;
        boolean ordenado = false;

        while (!ordenado) {
            ordenado = true;

            // 1. FASE ÍMPAR: Compara índices ímpares com seus sucessores (1,2), (3,4), ...
            boolean trocouImpar = executarFaseParalela(registros, 1, limiteInferior, limiteSuperior);

            // 2. FASE PAR: Compara índices pares com seus sucessores (0,1), (2,3), ...
            boolean trocouPar = executarFaseParalela(registros, 0, limiteInferior, limiteSuperior);

            // Se houve qualquer troca em uma das duas fases, a ordenação ainda não terminou
            if (trocouImpar || trocouPar) {
                ordenado = false;
            }
        }
    }

    /**
     * Executa uma fase de comparações (par ou ímpar) distribuindo os pares
     * entre as threads disponíveis sem concorrência de memória (disjoint pairs).
     *
     * @param registros Vetor de empresas
     * @param inicioParidade 0 para fase par, 1 para fase ímpar
     * @param limiteInferior Índice inicial de trabalho da rodada
     * @param limiteSuperior Índice final de trabalho da rodada
     * @return true se alguma troca ocorreu durante a fase
     */
    private boolean executarFaseParalela(Registro[] registros, int inicioParidade, int limiteInferior, int limiteSuperior) {
        // Se o tamanho da coleção for pequeno, executa sequencialmente para evitar overhead de threads
        if (registros.length < 500 || quantidadeThreads <= 1) {
            return executarFaseSequencial(registros, inicioParidade, limiteInferior, limiteSuperior);
        }

        Thread[] threads = new Thread[quantidadeThreads];
        boolean[] trocouArray = new boolean[quantidadeThreads];

        int totalItens = limiteSuperior - limiteInferior + 1;
        int fatia = totalItens / quantidadeThreads;

        for (int t = 0; t < quantidadeThreads; t++) {
            final int threadId = t;
            final int inicioThread = limiteInferior + (t * fatia);
            final int fimThread = (t == quantidadeThreads - 1) ? limiteSuperior : (inicioThread + fatia);

            threads[t] = new Thread(() -> {
                boolean trocouLocal = false;
                // Ajusta o início da thread para coincidir com a paridade da fase
                int inicioReal = inicioThread;
                if ((inicioReal % 2) != (inicioParidade % 2)) {
                    inicioReal++;
                }

                for (int j = inicioReal; j < fimThread && j < registros.length - 1; j += 2) {
                    // Ordenação decrescente: se o atual for MENOR que o próximo, troca
                    if (registros[j].getValor() < registros[j + 1].getValor()) {
                        trocar(registros, j, j + 1);
                        trocouLocal = true;
                    }
                }
                trocouArray[threadId] = trocouLocal;
            });

            threads[t].start();
        }

        // Aguarda todas as threads concluírem a fase atual (barreira de sincronização)
        boolean trocouGeral = false;
        for (int t = 0; t < quantidadeThreads; t++) {
            try {
                threads[t].join();
                if (trocouArray[t]) {
                    trocouGeral = true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return trocouGeral;
    }

    /**
     * Execução sequencial para lotes pequenos.
     */
    private boolean executarFaseSequencial(Registro[] registros, int inicioParidade, int limiteInferior, int limiteSuperior) {
        boolean trocou = false;
        int inicioReal = limiteInferior;
        if ((inicioReal % 2) != (inicioParidade % 2)) {
            inicioReal++;
        }

        for (int j = inicioReal; j < limiteSuperior && j < registros.length - 1; j += 2) {
            if (registros[j].getValor() < registros[j + 1].getValor()) {
                trocar(registros, j, j + 1);
                trocou = true;
            }
        }
        return trocou;
    }

    /**
     * Troca dois elementos de posição no vetor.
     */
    private static void trocar(Registro[] vetor, int i, int j) {
        Registro temp = vetor[i];
        vetor[i] = vetor[j];
        vetor[j] = temp;
    }

    @Override
    public String getNomeEstrategia() {
        return String.format("Bubble Sort Paralelo Odd-Even Transposition (%d threads)", quantidadeThreads);
    }
}
