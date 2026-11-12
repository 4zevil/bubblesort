package org.example;

/**
 * Implementação do algoritmo clássico e sequencial do Bubble Sort.
 * <p>
 * Ordena os registros comparando pares adjacentes de maior para menor.
 * Inclui a otimização de parada antecipada caso nenhuma troca ocorra em uma passada completa.
 * Esta classe serve como versão de referência simples e didática para apresentação acadêmica.
 * </p>
 *
 * @author Gabriel Lira Figueira
 * @author Eduarda Santana Santos
 * @author João Luís Aragão Rocha
 * @version 1.0
 */
public class BubbleSortClassico implements Ordenador {

    /**
     * Ordena os registros em ordem decrescente (do maior valor para o menor)
     * utilizando o algoritmo Bubble Sort tradicional com controle de trocas.
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

        for (int i = 0; i < n - 1; i++) {
            trocou = false;

            for (int j = 0; j < n - 1 - i; j++) {
                // Ordenação decrescente: se o elemento atual for MENOR que o próximo, troca
                if (registros[j].getValor() < registros[j + 1].getValor()) {
                    trocar(registros, j, j + 1);
                    trocou = true;
                }
            }

            // Se nenhuma troca ocorreu nesta passada, a coleção já está 100% ordenada
            if (!trocou) {
                break;
            }
        }
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
        return "Bubble Sort Clássico Sequencial (com parada antecipada)";
    }
}
