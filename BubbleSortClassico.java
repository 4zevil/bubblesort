package org.example;

/**
 * Implementação do algoritmo clássico e sequencial do Bubble Sort.
 * <p>
 * Ordena os registros comparando pares adjacentes de maior para menor.
 * Inclui controle de progresso em tempo real (exibindo porcentagem, passadas concluídas
 * e registros consolidados nas posições finais), além da parada antecipada
 * caso o vetor atinja estabilidade antes do total de passadas.
 * </p>
 *
 * @author Gabriel Lira Figueira
 * @author Eduarda Santana Santos
 * @author João Luís Aragão Rocha
 * @version 1.2
 */
public class BubbleSortClassico implements Ordenador {

    /**
     * Ordena os registros em ordem decrescente (do maior valor para o menor)
     * utilizando o algoritmo Bubble Sort tradicional com monitoramento de progresso real.
     *
     * @param registros Vetor de empresas a ser ordenado
     */
    @Override
    public void ordenarDecrescente(Registro[] registros) {
        if (registros == null || registros.length <= 1) {
            return;
        }

        int n = registros.length;
        boolean trocou;
        long tempoInicio = System.currentTimeMillis();
        long ultimoLog = tempoInicio;

        // Frequência de atualização visual: a cada 1% para lotes médios/grandes
        int intervaloNotificacao = Math.max(1, n / 100);

        for (int i = 0; i < n - 1; i++) {
            trocou = false;

            for (int j = 0; j < n - 1 - i; j++) {
                // Ordenação decrescente: se o elemento atual for MENOR que o próximo, troca
                if (registros[j].getValor() < registros[j + 1].getValor()) {
                    trocar(registros, j, j + 1);
                    trocou = true;
                }
            }

            // A cada passada 'i', o i-ésimo menor elemento já foi consolidado no final do vetor.
            // Exibe a barra de progresso em tempo real sem poluir o terminal:
            int passadasConcluidas = i + 1;
            long agora = System.currentTimeMillis();

            if (passadasConcluidas % intervaloNotificacao == 0 || passadasConcluidas == n - 1 || (agora - ultimoLog > 2000)) {
                ultimoLog = agora;
                double porcentagem = ((double) passadasConcluidas / (n - 1)) * 100.0;
                long segundosDecorridos = (agora - tempoInicio) / 1000;
                System.out.printf("\r  [Progresso]: %6.2f%% | Passadas: %,d de %,d | Linhas consolidadas: %,d | Tempo: %ds",
                        porcentagem, passadasConcluidas, (n - 1), passadasConcluidas, segundosDecorridos);
            }

            // Se nenhuma troca ocorreu nesta passada, a coleção já está 100% ordenada
            if (!trocou) {
                System.out.println("\n  -> Vetor estabilizado e ordenado antes do limite máximo de passadas!");
                break;
            }
        }
        System.out.println(); // Quebra de linha final ao terminar
    }

    /**
     * Troca a posição de dois elementos no vetor.
     *
     * @param vetor Vetor onde ocorrerá a troca
     * @param i     Primeiro índice
     * @param j     Segundo índice
     */
    protected static void trocar(Registro[] vetor, int i, int j) {
        Registro temp = vetor[i];
        vetor[i] = vetor[j];
        vetor[j] = temp;
    }

    @Override
    public String getNomeEstrategia() {
        return "Bubble Sort Clássico Sequencial (com monitoramento de progresso real)";
    }
}
