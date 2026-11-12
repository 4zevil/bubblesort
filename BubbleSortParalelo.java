package org.example;

/**
 * Implementação do Bubble Sort Paralelo com Transposição Par-Ímpar (Odd-Even Transposition Sort).
 * <p>
 * O algoritmo explora o paralelismo dividindo comparações de pares disjuntos entre threads de CPU:
 * <ul>
 *   <li><b>Fase Ímpar:</b> Pares (1,2), (3,4), (5,6)... processados em paralelo.</li>
 *   <li><b>Fase Par:</b> Pares (0,1), (2,3), (4,5)... processados em paralelo.</li>
 * </ul>
 * Inclui medição de progresso real em tempo real por rodada e por linhas processadas,
 * sem bloqueios fantasmas ou estimativas irreais.
 * </p>
 *
 * @author Gabriel Lira Figueira
 * @author Eduarda Santana Santos
 * @author João Luís Aragão Rocha
 * @version 1.2
 */
public class BubbleSortParalelo implements Ordenador {

    private final int quantidadeThreads;

    public BubbleSortParalelo() {
        this(Math.max(1, Runtime.getRuntime().availableProcessors()));
    }

    public BubbleSortParalelo(int quantidadeThreads) {
        this.quantidadeThreads = quantidadeThreads;
    }

    @Override
    public void ordenarDecrescente(Registro[] registros) {
        if (registros == null || registros.length <= 1) {
            return;
        }

        int n = registros.length;
        int limiteInferior = 0;
        int limiteSuperior = n - 1;
        boolean ordenado = false;
        int rodada = 0;
        long tempoInicio = System.currentTimeMillis();
        long ultimoLog = tempoInicio;

        while (!ordenado) {
            ordenado = true;
            rodada++;

            // 1. FASE ÍMPAR
            boolean trocouImpar = executarFaseParalela(registros, 1, limiteInferior, limiteSuperior);

            // 2. FASE PAR
            boolean trocouPar = executarFaseParalela(registros, 0, limiteInferior, limiteSuperior);

            if (trocouImpar || trocouPar) {
                ordenado = false;
            }

            // Notificação visual de progresso real em linhas e porcentagem
            long agora = System.currentTimeMillis();
            if (rodada % 10 == 0 || ordenado || (agora - ultimoLog > 1500)) {
                ultimoLog = agora;
                // No pior caso da transposição par-ímpar, são necessárias no máximo N rodadas
                double porcentagem = Math.min(100.0, ((double) rodada / n) * 100.0);
                long segundos = (agora - tempoInicio) / 1000;
                System.out.printf("\r  [Progresso Real]: %6.2f%% | Rodada: %,d de até %,d | Linhas: %,d | Tempo decorrido: %ds",
                        porcentagem, rodada, n, n, segundos);
            }
        }
        System.out.println(); // Quebra de linha final ao terminar
    }

    private boolean executarFaseParalela(Registro[] registros, int inicioParidade, int limiteInferior, int limiteSuperior) {
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
                int inicioReal = inicioThread;
                if ((inicioReal % 2) != (inicioParidade % 2)) {
                    inicioReal++;
                }

                for (int j = inicioReal; j < fimThread && j < registros.length - 1; j += 2) {
                    if (registros[j].getValor() < registros[j + 1].getValor()) {
                        trocar(registros, j, j + 1);
                        trocouLocal = true;
                    }
                }
                trocouArray[threadId] = trocouLocal;
            });

            threads[t].start();
        }

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
